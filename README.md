# Hamino开发文档

## 宣传

如果你需要详细的解释，可以访问我的博客学习更详细的内容：[点击我进行跳转](https://blog.minloha.cn/2023/01/04/%E9%9D%9E%E5%B8%B8%E8%AF%A6%E7%BB%86%E7%9A%84ANN/)

如果觉得代码不错可以为我点亮star，也可以关注我的博客[https://blog.minloha.cn](https://blog.minloha.cn)

目前我的博客正在更新Linux内核源码阅读，有感兴趣的可以看看哦

下载可以直接点击项目文件里的Hamino.jar

## 项目结构

- paper：运行环境，使用paper1.8核心，构建出来的插件应放入其中运行，IDEA配置运行项
- src：源代码
  - main.java.cn.minloha
    - Main.java主方法
    - Type：向量、矩阵和通信的单例类
    - NeuralWork：神经网络类，包括神经元、网络模型、激活函数与损失函数以及用于保存的DataClass类
    - ModelLoader：模型加载器
    - GetEvent：Minecraft的事件
    - GetCommander：Minecraft的指令
  - main.resources
    - config.yml配置文件
    - plugin.yml插件信息文件

## 神经网络

神经网络统一使用NetWork进行管理，参数为：

```java
NetWork net = new NetWork(输入维度,神经结构);
```

神经结构处使用折叠表达式，使用递减顺序排列，默认攻击事件发生的时候，神经结构为：

```java
NetWork net = new NetWork(6,11,9,8,6,4,3);
```

> 注意：这里不要修改，模型会加载不出来

为了提升数据的表达能力，在多次的观察中排列出了最佳的稀疏特征，也就是相对距离和准星指向共两对三维向量：

```java
// differ是传入神经网络的向量
differ.add(m_location.getX() - p_location.getX());
differ.add(fouce.getX() - p_location.getX()/2);
differ.add(m_location.getY() - p_location.getY());
differ.add(fouce.getY() - p_location.getY()/2);
differ.add(m_location.getZ() - p_location.getZ());
differ.add(fouce.getZ() - p_location.getZ()/2);
```

我们把坐标穿插排列，这样就可以有关联的训练了。在训练的过程一样会在控制台展示出实际输出标签，这点无需担心，我们可以根据标签最大值对应config.yml的内容判断它属于哪个类别，例如：

```bash
[Hamino]:(0.872256371,0.1273167283,0.182637843)
```

发现第一个标签值最大，那他的类别就是第一类，对应配置文件即可。

前向传播方法是一个重载，你可以加入预期的向量标签进行计算，然后返回损失值，或者仅使用differ进行计算，返回期望得标签向量：

```java
// 1-损失 = 精度
double accessy = 1-net.forward(new Vector(differ), except);
```

平方损失函数的计算非常简单，我们需要传递两个向量，其中y2是预期，y1是神经网络的输出值：

```java
public Double CovLoss(Vector y1,Vector y2){
    // \frac{1}{2}\sum_i(y2_i-y1_i)^2
    List<Double> temp = new ArrayList<>();
    for(int d = 0;d<y1.getDimens();d++){
        temp.add(y2.getAsList().get(d)-y1.getAsList().get(d));
    }
    Vector a = new Vector(temp);
    Double res = Vector.Multiplicate(a,a)/2;
    return res;
}
```

$$
Loss(y,\hat y) = \frac{1}{2}\sum_i(\hat y_i-y_i)^2
$$

这是表达式的形式。最后我们说一下模型的输出，为了防止模型出现保存异常或者无法读取，请不要修改这部分代码：

```java
public interface ModelLoad {
    // 从文件中加载模型
    public void LoadingInFile(String Filepath) throws IOException;
    // 获取参数，为了区分神经网络和机器学习的参数，这里使用List<?>
    public List<?> getParams();
    // 保存模型
    public void saveModel(String modelpath) throws IOException;
}
```

之后可能会写一个机器学习的算法，所以需要预留一个MLmodel.java用于加载机器学习的参数。

神经网络的模型后缀为.data，你可以使用txt方式打开，其中的参数都是使用这样的方式保存：

```bash
{
W:(0.0692548826,1.5395529970,0.1000000003,0.5461933367,0.0694943166,1.6509559252)
B:0.9999999999999175
}
```

W为权重矩阵，B为偏置，加载模型是按照前馈的顺序进行的，如果神经网络的结构不同，那无法加载模型。

---

当玩家进行PVP的时候一样可以调用模型，不过年前就不加分类了。

## 指令与配置文件

```java
        /*** hamino
         *     |- help // 获取帮助
         *     |- train start 分类 // 生成一个无限血量的僵尸, 进行训练，击打会有提示
         *     |- train stop  (停止训练然后保存模型，下次继续用)
         *     `- test
         */
```

分类按照配置文件内的设置进行：

```yaml
# Hamino配置文件
# Java没有办法调用显卡计算，所以模型训练需要使用CPU进行，训练请使用CPU性能好的机器训练。
# 之后会甩出几个API接口，大家可以尝试使用python-tensorflow训练.
# 模型格式见文档，输出请不要使用二进制(Java没有)
# 分类名就是外挂类型,最后会根据文件名进行提示作弊和封禁
# categories:
#  - "Killaura"
#  - "Reach"
categories:
  - "normal"
  - "killaura"
  - "reach"

# 模型文件名务必与分类名严格一致(包括次序)，这样代码就能少些两句了
# 模型保存在${server}/plugins/config/hamino下
# models:
#   - "/Killaura.data"
#   - "/Reach.data"
models:
  - "normal.data"
  - "killaura.data"
  - "reach.data"
```

加载对应类别的配置文件的时候，一定要让文件名和分类名一一对应，包括顺序，这样可以让我少写一些代码了，有需要也可以自己加点啥。

## 声明

本插件在目前的阶段不收费，如果需要更多功能或者需要定制其他反作弊功能，可以去Q：2821163570，我会在有时间的情况下为您定制，肯定收费的哈。

> 插件仅供学习交流，不允许私自倒卖！
