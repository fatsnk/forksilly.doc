[![Homepage](./images/giveup.jpg)](https://fatsnk.github.io/fatsnk/)
<br><img src="./images/fork.jpg" width="160" alt="fork"/>
<details>
  <summary>最近更新</summary>

version test1.26

*[有问题可前往discussion交流](https://github.com/fatsnk/forksilly.doc/discussions)*

* 2025.12.18
  * test1.26.82：添加后台保活服务（可能有用，不确定）。用于AI回复时，应用在后台也能接收消息；如果不需要这个功能，请关闭应用的通知权限。 
* 2025.12.15
  * test1.26.75：现在可以在主题设置的“符号与标签”页面为引号等符号包裹的内容（如对话）添加颜色样式。
* 2025.12.12
  * test1.26.72：支持渲染button标签。支持内联样式background和color，例如：`<button style="background: #3b82f6; color: white;">蓝色按钮</button>`；支持属性disabled，例如`<button disabled">被禁用的按钮</button>`。点击按钮会将按钮文本复制到剪贴板（没有其它功能）。
* 2025.11.25
  * test1.26.71：预设编辑屏幕现在可以长按条目进行多选、跨预设复制粘贴条目。
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
顺带发布ForkSilly的apk安装包和源码（repo不含完整源码，完整源码请在[📦releases](https://github.com/fatsnk/forksilly.doc/releases/)获取），遇到问题请先阅读[常见问题解答](https://github.com/fatsnk/forksilly.doc/discussions/4#discussion-9176039)。*消息文字太多超出了消息气泡、消息内容重叠、代码块内容不完整？请在主题设置中调整聊天气泡最大高度和代码块高度设置。详见https://github.com/fatsnk/forksilly.doc/discussions/1#discussioncomment-14816259*

[🎬 新手快速上手指南](moenew.md#-%E6%96%B0%E6%89%8B%E5%BF%AB%E9%80%9F%E4%B8%8A%E6%89%8B%E6%8C%87%E5%8D%97)：新手入门，介绍一些免费的API。

（20251222）提醒：pollinations.ai接口已经更新。如果你使用pollinations进行文生图，请将app更新到[1.26.86](https://github.com/fatsnk/forksilly.doc/releases/)以上版本，文生图url修改为`https://gen.pollinations.ai/image`，并前往新的[管理页面](https://enter.pollinations.ai/)`https://enter.pollinations.ai/`创建新的api key。
* pollinations现在有seedream-pro、nanobanana-pro、zimage-turbo 6B 等新模型，每天重置免费额度，请注意检查页面上的“Pricing”信息。

## 概要
ForkSilly: *一个react native/expo项目，主要用于Android。适用于对sillytavern有一定使用经验的用户*</br> ~~*小孩子不懂事，写着玩的🤕* *中文名*：*“傻叉”*（~~ <em>由于手机端sillytavern用起来较难受，所以让AI写了这个项目。</em>
<p></p>

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
使用上有疑问请先阅读相关文档和以下FAQ：
### FAQ：https://github.com/fatsnk/forksilly.doc/discussions/4#discussion-9176039


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

* 角色卡、预设：forksilly ⇌ sillytavern 可直接在相应的界面导出，并直接在sillytavern中使用，反之亦然；所有内容均与st中的对应。（角色备注会被忽略；“提示词覆盖”设置中的主要提示词`main`如果不为空，会替换掉预设中main条目的内容，jailbreak/post-history instructions的内容则始终使用预设中的内容，角色卡中的对应内容将被忽略）
* 全局世界书、全局正则：forksilly ⇌ sillytavern 同上，所有参数含义均与st相似。部分sillytavern的高级参数可能不适用，但不影响互相导入。
* 角色世界书：随角色卡一起导入，导出角色时也会包含在角色卡中。可单独导出角色世界书，并可直接导入到sillytavern。不能直接在forksilly应用中将额外的世界书文件导入到角色信息中，只能编辑、添加条目。如需将单独的世界书附加到角色卡，请使用[OcO萌/AI角色卡编辑器：CharacterEditor](https://ce.ooc.moe/zh-CN)，或直接在sillytavern中操作。
* 角色正则：支持编辑、新增，不支持直接将正则脚本文件导入到角色卡；请使用复制粘贴的方式，或使用第三方开源角色卡编辑器。
* 聊天记录：如果要导出记录给st使用，请前往存储管理-chats目录-角色名目录，长按选择需要导出的记录，点击左下方的“转换”按钮，会生成一个包含‘_converted’字样的新聊天记录，该记录导出后可直接导入到st。st的记录也需在存储管理中转换后才可在forksilly中使用，否则消息顺序是反的。在线转换工具：[聊天记录转换器](https://fatsnk.github.io/legendary-pancake/jsonl-converter.html)
- 关于角色备注、作者注释、世界书位置中的作者注释前/后：这些功能均被世界书的功能完全覆盖，因此本应用不再支持这些设置参数。

## 使用技巧

* 测试url: `https://oai-liard.vercel.app/v1` ，用于测试连通性、模型获取、确认上下文，也可以用于伪造assistant回复；
  * 使用方法:添加一个新的api配置，填写该url，apikey任意填写，关闭流式传输，即可使用。

* 消息气泡可以独立设置透明度而不影响消息文字：直接在用户气泡色和AI气泡色输入框中填写包含alpha通道值的16进制值即可，例如#3A3A3ABB，末尾的BB即表示不透明度，值越大透明度越低，FF为完全不透明；也可以设置成00全透明，模拟出类似文档的主题效果。

* 支持部分HTML和markdown语法，例如：
```
文字颜色：
<font color='red'>①这是红色字。</font>
<span style="color: black;">这是黑的文字</span>
按钮（仅用于复制按钮文本）：
<button style="background: #3b82f6; color: white;">蓝色按钮</button>
<button disabled">被禁用的按钮</button>
插入web图片：
![](https://files.catbox.moe/y6uo86.jpg)
<img src="https://files.catbox.moe/y6uo86.jpg" alt="公司Logo">
使用在存储管理中上传的图片：
<img src="gallery/我的角色/微笑.png" alt="微笑表情">
...
```

* 在🔧中的存储管理中可管理你的文件，并可在`Gallery/角色名`目录下导入表情包和角色图片，以支持让AI以HTML格式插入聊天中，使用相对路径，例如`<img src="gallery/我的角色/微笑.png" alt="微笑表情">`

* 支持roll和random占位符，例如
- {{roll:10000}}
- {{roll:1d100}}

支持常见占位符，包括{{user}}、{{char}}、{{lastcharmessage}}、{{lastusermessage}}、{{lastmessage}}，因此大多数预设无需修改可直接使用

   *更多占位符请查阅[项目结构文档](ProjectStructure.md)*

* 想使用不受信任的https连接或frp内网穿透？请参考：[服务器自签证书教程](服务器自签证书教程.md)、[文生图简单教程](text2img.md)

角色管理界面点击排序后默认按修改时间排序，编辑角色后，可下拉刷新以便让其显示到最前方；将角色添加到收藏，可以让ta优先显示

* 隐藏楼层：使用斜杠命令`/hide 楼层数`手动隐藏，对应的楼层就不会发送给AI；或者在全局正则中添加一个正则匹配所有文本，例如`^([\s\S]*)$`，然后设置最小深度，例如设置最小深度2，表示隐藏倒数第2层以上的所有历史消息（即从倒数第三楼开始隐藏）。其余设置根据需要调整。

### 推荐工具

角色卡创建和编辑：[妮卡角色工作室Pro](https://mocards.netlify.app/)、[OcO萌/AI角色卡编辑器：CharacterEditor](https://ce.ooc.moe/zh-CN)

文生图webui推荐（[stable-diffusion-webui-forge](https://github.com/lllyasviel/stable-diffusion-webui-forge)）：ControlNet的开发者lllyasviel制作的优化版webui，对性能较差的显卡有很大提升
  * 秋葉aaaki的整合包（[B站链接](https://www.bilibili.com/video/BV1rc6nYNEYo)）：支持SD3.5、FLUX，解压即用，无须安装git、Python、cuda等任何内容，只需将模型放入Stable Diffusion目录，在启动器高级设置中启用远程连接，即可使用！非常适合不想进行复杂设置、折腾ComfyUI工作流的玩家

如果你想使用Zimage，请使用这个版本:[NEO](https://github.com/Haoming02/sd-webui-forge-classic/tree/neo?tab=readme-ov-file#new-features)。该版本没有一键安装包，需自行安装。

文生图模型推荐：[WAI-NSFW-illustrious-SDXL](https://civitai.com/models/827184?modelVersionId=1761560)

[octopus](https://github.com/bestruirui/octopus)：One Hub All LLMs.

## 声明

*forksilly项目基于[AGPL-3.0](./LICENSE)授权，目前不提供闭源许可；任何基于此项目的修改、衍生作品或包含该项目代码的软件，必须同样以 AGPL-3.0 协议开源。*
*如果您希望在其它项目中使用本代码：必须遵守 AGPL-3.0 协议，公开您的完整源代码；不可将本项目代码整合到闭源软件中。*

### 免责声明

```
forksilly项目是一个开源的通用大语言模型（LLM）前端工具，仅提供技术框架和接口集成功能。
1. 内容责任
项目不提供任何 AI 模型服务或 API 密钥
用户通过forksilly生成的所有内容（包括文本、图片等）均由用户自行负责
用户接入的第三方 API 服务及其生成内容，责任由用户与相应服务提供商承担
项目开发者和维护者对用户使用forksilly生成的任何内容不承担法律责任
2. 使用规范
使用此项目时，您需要：
 遵守您所在地区的法律法规
 遵守您所使用的第三方 API 服务条款
 对生成内容的合法性、合规性负责
 不得将项目用于任何非法用途
 不得生成违反法律法规或侵犯他人权益的内容
3. 风险提示
AI 生成内容可能存在错误、偏见或不当信息
用户应自行审查和验证生成内容的准确性与合法性
因使用本工具产生的任何纠纷、损失或法律后果，由用户自行承担
使用此项目，即表示您已阅读、理解并同意本免责声明。
```
