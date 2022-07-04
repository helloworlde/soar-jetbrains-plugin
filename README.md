# SOAR JetBrains Plugin

[![JetBrains Plugins](https://img.shields.io/jetbrains/plugin/v/11417-soar.svg)](https://plugins.jetbrains.com/plugin/11417-soar)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/11417-soar.svg)](https://plugins.jetbrains.com/plugin/11417-soar)
[![Downloads last month](http://phpstorm.espend.de/badge/11417/last-month)](https://plugins.jetbrains.com/plugin/11417-soar)

### [中文](README-zh.md)

> SOAR Plugin is a plugin of JetBrains IDE for Xiaomi's [SOAR](https://github.com/XiaoMi/soar) (SQL Optimizer And Rewrite), used for analysis SQL performance , give suggestion for optimize, and format SQL.

## Demo

### Analysis
![Analysis Demo-Gif](img/SOAR%20Analysis%20Demo.gif)

### Format
![Format Demo-Gif](img/SOAR%20Format%20Demo.gif)

## Install

### Install form local file 
1. Download latest compressed package of [SOAR](https://plugins.jetbrains.com/plugin/11417-soar) plugin 
2. Open IDE，Settings -> Plugins ->  Setting Button -> Install Plugin From Disk 
![Install SOAR](img/SOAR%20Install%20localfile.png)
3. Choose location of the compressed package
4. Restart IDE after install complete

### Install from marketplace
1. Settings -> Plugins -> Marketplace
2. Search SOAR
![Install from marketplace](img/SOAR%20Marketplace.png)
3. Click install, restart IDE after install complete

## Configuration 

### Install SOAR

> You need configure Go path correctly if you want use SOAR. You can reference from [https://golang.org/doc/install](https://golang.org/doc/install) for install and configure

1. Open IDE, Settings -> Other Settings -> Soar (Older version is IDE Settings -> Soar)
2. Click Init button to get install command 
![SOAR Init](img/SOAR%20Init%200.png)
3. There will input `${USER_HOME}/.soar/` as default path after check success. Then copy the command of download and install SOAR 
![SOAR Init](img/SOAR%20Init%201.png)
```
wget https://github.com/XiaoMi/soar/releases/download/0.10.0/soar.darwin-amd64 -O ${USER_HOME}/.soar/soar
```
4. Execute modify permission execution command after download completed.

```
chmod a+x ${USER_HOME}/.soar/soar
```
5. Click Check button to check if SOAR installed correctly 
![Soar Check](img/SOAR%20Check%20Result.png)

6. Apply settings

### Use file configuration 
1. Choose Use  File Config 
2. Click Edit button after Config Path 
3. Input Database and other configuration as yours, you can reference [Configuration instruction](https://github.com/XiaoMi/soar/blob/master/doc/config.md) of SOAR configuration
![Configuration](img/SOAR%20Configuartion.png)
4. Click Edit button after Blacklist Path 
5. Add signature or Regex for not analysis SQL
![Blacklist](img/SOAR%20blacklist.png)

### Use manual configuration
1. Choose Use Manual Config
2. Input Database configuration
![Manual Config](img/SOAR%20Manual%20Config.png)
3. Apply settings 

**Attention**
- The account in test environment need all privilege, online account only need select permission
- The host `localhost` is not support, please replace as `127.0.0.1`
- The special symbol in password is not support too, please use file configuration.

## Usage

### SQL Analysis  
1. Select SQL content  which need analysis, right click to open edit popup , then choose Soar -> SQL Analysis . Or you can use shortcut `ctrl + shift + H` too
![Analysis Action](img/SOAR%20Analysis%20Action.png)

2. The analysis process will show in under border until analysis action complete.
![Analysis Process](img/SOAR%20Analysis%20Process.png)

3. There will show dialog when action completed.
![Analysis Result](img/SOAR%20Analysis%20Result.png)

### Format SQL 
1.  Select SQL content  which need format, right click to open edit popup , then choose Soar -> SQL Format . Or you can use shortcut `ctrl + shift + J` too
![Format Action](img/SOAR%20Format%20Action.png)

2. It will replace origin SQL content as formatted SQL after action complete. The format process will show in under border too.
![Format Process](img/SOAR%20Format%20Process.png)
![Format Result](img/SOAR%20Format%20Result.png)