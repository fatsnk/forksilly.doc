[![Homepage](./images/giveup.jpg)](https://fatsnk.github.io/fatsnk/)
<br><img src="./images/fork.jpg" width="160" alt="fork"/>
<details>
  <summary>最近更新</summary>

version test1.26

* [有问题可前往discussion交流](https://github.com/fatsnk/forksilly.doc/discussions)

* 2025.11.11
  * test1.26.62：聊天屏幕添加快速滚动按钮；角色管理界面添加拼接布局；新的应用图标
* 2025.11.05
  * test1.26.56：角色图片可以选择jpg格式图片。
* 2025.11.04
  * test1.26.55：添加markdown表格渲染。
* 2025.11.02
  * test1.26.51：添加免费的文生图服务pollinations.ai。
* 2025.09.22
  * test1.26.20：支持简单的粗体、斜体，以及列表、分割符、删除线、各级标题、引用、链接的样式（markdown、HTML标签样式）；添加高亮文字、下划线的渲染支持（仅HTML标签样式）

* 2025.09.05
  * 尝鲜版1.26.0。
  * 添加了记忆和知识库功能（使用嵌入模型生成向量）
  * 使用op-sqlite作为本地数据库存储记忆和资料， 并使用sqlite-vec插件存储向量, an extension for RAG embeddings。
  * 添加了/hide和/unhide斜杠命令。

  * 使用教程：[记忆和知识库教程](https://github.com/fatsnk/forksilly.doc/blob/main/Embedding.md)

* changelog: [其它更新](changelog.md)
   
</details>

# forksilly.doc
此仓库主要存放ForkSilly的文档，
顺带发布ForkSilly的apk安装包和源码（repo不含完整源码，完整源码请在[📦releases](https://github.com/fatsnk/forksilly.doc/releases/latest)获取），安装使用请先阅读[注意事项](#注意事项) 。

[🎬 新手快速上手指南](moenew.md#-%E6%96%B0%E6%89%8B%E5%BF%AB%E9%80%9F%E4%B8%8A%E6%89%8B%E6%8C%87%E5%8D%97)：新手入门，介绍一些免费的API。

## 概要
ForkSilly: *一个react native/expo项目，主要用于Android。适用于对sillytavern有一定使用经验的用户*</br> ~~*小孩子不懂事，写着玩的🤕* *中文名*：*“傻叉”*（~~
<p><em>在手机上体验sillytavern很难受，于是干脆让AI写了一个</em></p>

<img src="./images/richtext01.gif" width="200"/><img src="./images/chatscreen1.25.38.jpg" width="200"/><img src="./images/bubbles.gif" width="200"/>

#### 其它预览： [应用UI预览](./images/readme.md)

## ForkSilly介绍
* 该项目为自用分享，可能无法受理功能申请，使用上有问题请先参考本项目中的文档。
  * *因为99.9%的代码由AI生成，你提需求我也做不出来😭（*
* 适合纯文字卡、插图卡（使用图床或[本地上传](#使用技巧)），以及Stable Diffusion文生图。不兼容前端/变量卡

+ 兼容sillytavern V2角色卡、世界书、正则、预设、聊天记录，可以随时导入导出。（[兼容性指引](#兼容性指引)）
+ 可任意更换的聊天字体
+ 可更换的聊天背景图片
+ 聊天中快速开关预设条目
+ 可使用任意OpenAI兼容格式的API，包括Gemini的OpenAI兼容接口，支持Google AI studio的API key，提示词后处理选“严格”。
+ 内置存储管理功能，用户可以导出应用内的所有内容（请谨慎操作）、管理缓存
+ 可在聊天中使用文生图：
  + 接入免费文生图服务pollinations.ai
  + 支持Stable Diffusion文生图功能(sd需开启监听：添加启动参数` --api --listen --cors-allow-origins '*'`)，请点击🔧图标进入更多选项-文生图，并添加API、预设并设置默认和参数，然后在触发标签中开启聊天文生图功能。预设中的main在请求时会被替换成捕获的提示词，其它条目则会根据你选择的位置拼接。[文生图简单教程](text2img.md)
    * 在llm的预设中添加一个文生图条目，让AI输出你自定义的标签包裹的文生图提示词即可，例如`<gen_image>prompt tags</gen_image>`（可在预设中添加相应条目，或使用世界书）。如果需要重新生成，可点击🎨手动生成。
    * 可在存储管理的gallery目录中找到聊天中生成的图片
+ （可选）接入硅基流动和Gemini的嵌入模型。（支持记忆生成、自动保存到本地向量数据库：[记忆和知识库教程](Embedding.md)）。

- 不支持tts(文字转语音)
- 不支持连接nai

## License

ForkSilly包含第三方依赖，查看它们的许可详情：[LICENSE-THIRD-PARTY](./LICENSE-THIRD-PARTY) 

This project is licensed under the GNU Affero General Public License v3.0 (AGPL-3.0).  
See the [LICENSE](./LICENSE) file for details.

## 注意事项

- **安装：部分品牌手机自带的文件管理器可能会阻止你安装未授权的应用或直接弹出安装失败，如果遇到这种问题，请使用[mt管理器](https://mt2.cn)或其它第三方文件管理器安装
）**
- API设置、主题设置中的文本输入框修改数值或文本后，需点击虚拟键盘上的确认键（回车键的位置），或点击一下其它输入框，然后点击保存才能生效
- **在角色编辑界面修改了角色信息后，需重新在角色管理界面选择角色进入聊天，若直接返回聊天，当前聊天使用的角色的信息仍是旧的**
- 更换角色图片时，如果仍显示旧图片，是缓存问题，重启应用即可。
<!--
- **预设、世界书、正则等导入时，如果无法导入，请不要在文件选择器的“最近”等地方选择，请点击弹出的选择器（你手机系统自带的文件管理）侧边栏的`文件管理`或“你的手机名称”的项目，从设备目录中选择要导入的文件；也可以尝试在存储管理中导入预设**
-->
  * 通常，Discord中下载的文件会保存在“download”目录，QQ下载并手动保存的文件（注意保存到手机后才能看到！只在QQ里点击下载是看不到的）通常在`download/QQ`目录下
    
- 卡片主题下，点击消息气泡右下角更多菜单的🌐图标可以显示一些带美化HTML的角色卡的美化效果。默认主题该按钮无实际作用，请不要点击；
- 卡片主题要滑动整个消息列表，需滑动AI消息气泡外的区域（例如屏幕边缘或上下空白处），滑动消息部分仅能滚动消息本身的内容。
- **如果你发现看不到AI回复的消息或消息不完整，请点击编辑按钮或检查你的预设，将自定义标签添加到主题与样式设置中的自定义标签中（例如`<content>、<statusblock>、<status>、<statusbar>`等，添加时不要填写尖括号`<>`），选择渲染为原始内容即可**
- 如果消息超出了消息气泡、代码块中文本显示不全，请到主题设置中调整**聊天气泡最大高度**和代码块高度设置。默认主题的部分设置也会影响卡片主题。
- 角色的**首条消息**会将原始占位符例如`{{user}}`和`{{char}}`等直接显示出来，这是特性不是bug（仅显示，发送给AI的请求仍会替换为相应的名字。可以点击右上角的预览提示词查看确认）；强制替换：点击✏️编辑消息，然后直接点击保存，占位符会被替换为实际的内容，替换后不可逆；也可使用正则处理显示。
- 目前API参数中的最大上下文窗口是无效的，如果因楼层太高上下文太长导致你使用的模型返回错误，请使用消息框右下角的删除或分支功能减少几楼，也可以使用正则或隐藏楼层功能（/hide命令），然后总结重开。

- 不要启用空正则，角色会爆炸（
- 谨慎使用形如`/[\s\S]*?<\/thinking>/gm`的正则表达式来修改显示，否则极有可能导致UI卡顿，建议使用更安全的表达式例如`/<thinking>(?:(?!<thinking>)[\s\S])*?<\/thinking>/gm`；性能差的正则会在发送消息时消耗更多时间来处理消息，导致收到回复的时间增加，影响体验。
- 角色名不要使用特殊符号，例如`:`
- 现在屏幕上显示的历史消息不会实时计算全局正则脚本的“最小深度”和“最大深度”，请通点击聊天历史📓按钮选择当前聊天记录来刷新。（新回复的消息会正常应用正则。该特性不影响“修改AI请求内容”设置，发送给API的提示词会立即应用正则的深度。）不建议依赖太多正则，瞎写的正则很容易引起回溯，造成严重性能问题；添加过多的正则脚本更容易产生问题。


## 下载
<p>选择文件名不含src的压缩包，解压后安装APK文件。适用所有架构</p>
<p align="left">
  <a href="https://github.com/fatsnk/forksilly.doc/releases/latest">
    <img src="https://img.shields.io/badge/📥 下载最新版本-blue?style=for-the-badge" alt="下载按钮">
  </a>
</p>

## 打包

源码以zip格式上传于release。
项目结构：[项目结构文档](ProjectStructure.md)

如果你想自己打包APK，可下载源码到本地，使用以下命令调试和打包（需先配置Android开发环境和Java开发环境）：
```
npx expo prebuild --platform android --clean
（在模拟器调试）npx expo run:android
（切换到Android目录打包）gradlew assembleRelease
```

## 兼容性指引

* 角色卡、预设：forksilly ⇌ sillytavern 可直接在相应的界面导出，并直接在sillytavern中使用，反之亦然；所有内容均与st中的对应。（角色备注会被忽略；“提示词覆盖”设置中的主要提示词`main`如果不为空，会替换掉预设中main条目的内容，jailbreak/post-history instructions的内容则始终使用预设中的内容，角色卡中的对应内容会被忽略）
* 全局世界书、全局正则：forksilly ⇌ sillytavern 同上，所有参数含义均与st相似。部分sillytavern的高级参数可能不适用，但不影响互相导入。
* 角色世界书：随角色卡一起导入，导出角色时也会包含在角色卡中。可单独导出角色世界书，并可直接导入到sillytavern。不能直接在forksilly应用中将额外的世界书文件导入到角色信息中，只能编辑、添加条目。如需将单独的世界书附加到角色卡，请使用[OcO萌/AI角色卡编辑器：CharacterEditor](https://ce.ooc.moe/zh-CN)，或直接在sillytavern中操作。
* 角色正则：支持编辑、新增，不支持直接将正则脚本文件导入到角色卡；请使用复制粘贴的方式，或使用第三方开源角色卡编辑器。
* 聊天记录：如果要导出记录给st使用，请前往存储管理-chats目录-角色名目录，长按选择需要导出的记录，点击左下方的“转换”按钮，会生成一个包含‘_converted’字样的新聊天记录，该记录导出后可直接导入到st。st的记录也需在存储管理中转换后才可在forksilly中使用，否则消息顺序是反的。在线转换工具：[聊天记录转换器](https://fatsnk.github.io/legendary-pancake/jsonl-converter.html)
- 关于角色备注、作者注释、世界书位置中的作者注释前/后：这些功能均被世界书的功能完全覆盖，因此本应用不再支持这些设置参数，会忽略它们。

## 使用技巧

支持部分HTML和markdown语法，例如：
```
文字颜色：
<font color='red'>①这是红色字。</font>
<span style="color: black;">这是黑的文字</span>
插入web图片：
![](https://files.catbox.moe/y6uo86.jpg)
<img src="https://files.catbox.moe/y6uo86.jpg" alt="公司Logo">
使用在存储管理中上传的图片：
<img src="gallery/我的角色/微笑.png" alt="微笑表情">
...
```

在🔧中的存储管理中可管理你的文件，并可在`Gallery/角色名`目录下导入表情包和角色图片，以支持让AI以HTML格式插入聊天中，使用相对路径，例如`<img src="gallery/我的角色/微笑.png" alt="微笑表情">`

支持roll和random占位符，例如
- {{roll:10000}}
- {{roll:1d100}}

更多占位符请查阅[项目结构文档](ProjectStructure.md)

支持常见占位符，包括{{user}}、{{char}}、{{lastcharmessage}}、{{lastusermessage}}、{{lastmessage}}，因此大多数预设无需修改可直接使用

想使用不受信任的https连接或frp内网穿透？请参考：[服务器自签证书教程](服务器自签证书教程.md)、[文生图简单教程](text2img.md)

角色管理界面点击排序后默认按修改时间排序，编辑角色后，可下拉刷新以便让其显示到最前方；将角色添加到收藏，可以让ta优先显示

隐藏楼层：使用斜杠命令`/hide 楼层数`手动隐藏，对应的楼层就不会发送给AI；或者在全局正则中添加一个正则匹配所有文本，例如`^([\s\S]*)$`，然后设置最小深度，例如设置最小深度2，表示隐藏倒数第2层以上的所有历史消息（即从倒数第三楼开始隐藏）。其余设置根据需要调整。

### 推荐工具

角色卡创建和编辑：[OcO萌/AI角色卡编辑器：CharacterEditor](https://ce.ooc.moe/zh-CN)

文生图webui推荐（[stable-diffusion-webui-forge](https://github.com/lllyasviel/stable-diffusion-webui-forge)）：ControlNet的开发者lllyasviel制作的优化版webui，对性能较差的显卡有很大提升
  * 秋葉aaaki的整合包（[B站链接](https://www.bilibili.com/video/BV1rc6nYNEYo)）：支持SD3.5、FLUX，解压即用，无须安装git、Python、cuda等任何内容，只需将模型放入Stable Diffusion目录，在启动器高级设置中启用远程连接，即可使用！非常适合不想进行复杂设置、折腾ComfyUI工作流的玩家

文生图模型推荐：[WAI-NSFW-illustrious-SDXL](https://civitai.com/models/827184?modelVersionId=1761560)

[new-api](https://github.com/QuantumNous/new-api)：AI接口管理与分发系统，支持将多种大语言模型转为统一的OpenAI兼容格式调用，有Windows版本，双击即可使用，方便管理和使用不同供应商的API。
