# Rebase Android ![](https://circleci.com/gh/drakeet/rebase-android.svg?&style=shield&circle-token=ac8640c37e3a5b40715b9f2f0017db9362316066)

![](app/src/main/res/drawable-nodpi/ic_rebase_flat_w192.png)

注：本项目还没正式公开或发布，仍然处于建设中。

## 关联

- 服务端源代码(Rebase Server): https://github.com/drakeet/rebase-server
- API 文档(Rebase API): https://github.com/drakeet/rebase-api
- Sketch 设计稿源文件: 即将开源

![](http://ww1.sinaimg.cn/large/86e2ff85gy1fcrj326ngyj218i0x0102)

## 结构

Rebase 采用了极简而精巧的结构、包设计，令每个人都能轻易参与和阅读。`api` / `app` / `web` 三层结构：

<img src="image/app.png" width=400 height=524/><img src="image/api.png" width=400 height=432/>

## 代码特性

- 代码入手难度极低，源于对于"简单直观、干净清晰"理念的把握和追求
- styles、layout 文件内容极其规范、有序、分明
- 基于 MultiType，全局没有新创建任何一个 Adapter 类
- 没有一股脑的 MVP、MVVM、过分的响应式设计，没有绕来绕去的关系，无招胜有招
- 格式一丝不苟，不多不少任何一个空格、换行
- 良好的面向对象关系设定
- 模块清晰，聚合有度
- 尽可能采用组合，委托、代理类可拷贝可复用，高度解耦

## 功能特性

- 当前栏目：**Tweet，日志，每日推荐，新鲜好去处，好吃好吃**
- 除了应用名和关于页面，几乎所有内容都是动态可变的，包括栏目
- 支持记录文章上次未读完位置
- 支持 Android 7.0 shortcut
- 支持三星系统和 Android 6.0 分屏功能
- 极快的启动速度
- APK 体积极小，只有 1MB 左右
- 优雅的事件统计打点方式

## 关于 Rebase

有一天我在某某朋友圈里说过要开发这么一个项目，它的定位就是移动客户端级别的 WordPress.

WordPress 是一款自由开源的博客软件和内容管理系统，它帮助无数人在网页端开发和搭建自己的网站，我的个人网站 https://drakeet.me 也正是采用 WordPress 搭建，这是一个伟大的产品。

而我们要做的就是将它的理念复刻到手机上，实现人人可以快速创建私人博客 App 并与世界相连，我给它取名叫 Rebase.

当有一天我决定再也不看、不发某某朋友圈了，Rebase 的想法便更加急切在我脑袋中萦绕，直到它现在呈现在你面前。

这个应用程序是 Rebase 项目的客户端之一，它是完全开源的，包括客户端源代码、服务端源代码，以及即将开源的 Sketch 设计稿源文件，如果你很喜欢它，就加入进来吧！

Telegram Group: https://t.me/joinchat/AAAAAAnT8ampUz4vZFw0SQ

## 关于 日常

日常 是一款由 drakeet 负责推荐和分享各种好玩新鲜的栏目应用，基于 Rebase 服务。

## How to Contribute

请使用 **DrakeetAndroid([drakeet_style.zip](code_style.zip))** 代码风格配置文件，以保持统一。提交 Pull Request 请确保你的格式没有任何问题，包括该有的各种空格。

## 其他

这个项目将在 README 优先使用中文。目前处于第一个公开版本，尽管如此，它仍然经过精心设计和编码，极力追求简单直观，采用原生模式开发，没有很绕的关系，做了适当而不复杂的分层，继承关系克制，能组合的组合，能内聚的尽量内聚。布局文件十分讲究，styles、values 分明，代码干净、格式一丝不苟，易读易协作，可拷贝可复用。

如果你有任何问题或提供工作方面的机会，请提 issue 或致信我的邮箱 drakeet.me@gmail.com

# 开源协议

Rebase Android 基于 GPL-3.0 开源协议，在使用本项目代码的之前请确保你了解这个协议。
详细内容参见：https://github.com/drakeet/rebase-android/blob/master/LICENSE
