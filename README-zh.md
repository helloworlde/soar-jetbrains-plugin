# SOAR JetBrains Plugin

[![JetBrains Plugins](https://img.shields.io/jetbrains/plugin/v/11417-soar.svg)](https://plugins.jetbrains.com/plugin/11417-soar)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/11417-soar.svg)](https://plugins.jetbrains.com/plugin/11417-soar)
[![Downloads last month](http://phpstorm.espend.de/badge/11417/last-month)](https://plugins.jetbrains.com/plugin/11417-soar)

### [English](README.md)

> SOAR Plugin 是为小米 [SOAR](https://github.com/XiaoMi/soar) (SQL Optimizer And Rewrite)开发的 JetBrains平台  IDE 的插件，用于分析SQL 性能、给出优化建议和格式化 SQL

## Demo

### 分析
![Analysis Demo-Gif](img/SOAR%20Analysis%20Demo.gif)

### 格式化
![Format Demo-Gif](img/SOAR%20Format%20Demo.gif)

## 安装

### 本地文件安装
1. 从下载最新的 [SOAR]((https://plugins.jetbrains.com/plugin/11417-soar)) 插件压缩包
2. 打开 IDE，Settings -> Plugins -> 设置按钮 -> Install Plugin From Disk 
![Install SOAR](img/SOAR%20Install%20localfile.png)
3. 选择 SOAR 插件压缩包的位置
4. 安装后重启 IDE

### 插件市场安装
1. Settings -> Plugins -> Marketplace
2. 搜索 SOAR
![Install from marketplace](img/SOAR%20Marketplace.png)
3. 点击安装，等安装成功后重启 IDE

## 配置 

### 安装 SOAR
> 要使用 SOAR，需要本地正确安装配置 Go Path，可以参考[https://golang.org/doc/install](https://golang.org/doc/install)进行配置

1. 首先需要配置 SOAR，打开 IDE， Settings -> Other Settings -> Soar (旧版本IDE Settings -> Soar)
2. 点击 Init 按钮获取安装命令
![SOAR Init](img/SOAR%20Init%200.png)
3. 等待检查完成会将 `${USER_HOME}/.soar/`路径作为默认路径直接填充，复制下载安装 SOAR 的命令到终端执行
![SOAR Init](img/SOAR%20Init%201.png)
```
wget https://github.com/XiaoMi/soar/releases/download/0.10.0/soar.darwin-amd64 -O ${USER_HOME}/.soar/soar
```
4. 等待下载 SOAR 完成后修改SOAR 的执行权限
```
chmod a+x ${USER_HOME}/.soar/soar
```
5. 回到 IDE，点击 check 按钮，检查是否安装成功
![Soar Check](img/SOAR%20Check%20Result.png)
6. 保存设置

### 使用文件配置
1. 选择 Use  File Config 
2. 点击 Config Path 后面的 Edit
3. 修改你的数据库配置和其他配置，配置内容可以参考[配置文件说明](https://github.com/XiaoMi/soar/blob/master/doc/config.md)
![Configuration](img/SOAR%20Configuartion.png)
4. 点击 Blacklist Path 后面的 Edit
5. 添加不需要分析的 SQL 指纹或者正则
![Blacklist](img/SOAR%20blacklist.png)

### 使用手动配置
1. 选择 Use Manual Config
2. 输入线上和测试数据库配置信息
![Manual Config](img/SOAR%20Manual%20Config.png)
3. 保存设置

**注意**
- 测试环境的数据库账号需要 all privilege，线上环境只需要 SELECT 
- 数据库的地址不支持 `localhost`，需要替换为 `127.0.0.1`
- 密码不支持特殊字符，有特殊字符请使用文件配置

## 使用 

### SQL 分析 
1. 选中要分析的 SQL，右键，选择 Soar -> SQL Analysis，也可以使用快捷键`ctrl + shift + H`
![Analysis Action](img/SOAR%20Analysis%20Action.png)

2. 等待 SQL 分析执行完毕，可以在 IDE 的下边栏查看进度
![Analysis Process](img/SOAR%20Analysis%20Process.png)

3. SQL 分析执行完成，会弹出弹窗显示分析结果
![Analysis Result](img/SOAR%20Analysis%20Result.png)

### SQL 格式化
1. 选中要格式化的 SQL，右键，选择 Soar -> SQL Format，也可以使用快捷键`ctrl + shift + J`
![Format Action](img/SOAR%20Format%20Action.png)

2. 等待 SQL 格式化执行完成，会自动替换掉选中的内容；可以在 IDE 的下边栏查看进度
![Format Process](img/SOAR%20Format%20Process.png)
![Format Result](img/SOAR%20Format%20Result.png)