# 文生图简单教程

1.部署Stable Diffusion并打开监听端口，建议使用（[stable-diffusion-webui-forge](https://github.com/lllyasviel/stable-diffusion-webui-forge)），下载秋葉aaaki的整合包（[B站链接](https://www.bilibili.com/video/BV1rc6nYNEYo)），请参考相关教程，这里不再过多介绍。

2.打开forksilly，点击右上角🔧，选择文生图，进入文生图设置。
点击`API管理`标签，添加你部署好的Stable Diffusion的地址并保存：

<img src="./images/help/sdAPI.png" width="400"/>

3.点击`预设管理`标签，添加一个预设，随意填写一些预定义的提示词，例如high quality，点击保存：

<img src="./images/help/imggenPreset.png" width="400"/>

4.点击“生成设置”，选择刚才保存的API配置，然后在下方的生成参数中填写你的模型使用的参数，并点击“保存到当前API配置”。点击保存按钮后，界面参数可能会恢复原始值，返回重新进入该设置界面即可。你也可以在这里测试图片生成是否正常：

<img src="./images/help/imggenParameter.png" width="400"/>

5.点击`触发标签`选项卡，打开“启用触发功能”开关，然后点击保存。你也可以自行填写喜欢的触发标签，不修改则默认是"gen_image".

<img src="./images/help/imggenTrigger.png" width="400"/>

6.在llm的预设或世界书中新增条目，填写文生图提示词，指导AI使用正确格式输出关键词，例如：

```
<img_gen_request>
请在每次回复的正文后调用文生图工具，以可用于Stable Diffusion的关键词格式输出。根据角色、地点、情境等，只使用一张图片，关键词需要足够详细。

调用格式：
<gen_image>keyword1, keyword2, keyword3, ...</gen_image>

<img_gen_request>

```

将`<gen_image>`改成你自己设置的触发标签。

<img src="./images/help/imggenPrompt.png" width="400"/>

7.到这里已经设置完毕。如果在第四点生成设置中测试能正常生成图片，则已经可以正常使用。

参考效果图：

<img src="./images/help/ReferenceImg.jpg" width="400"/>

8.可选设置

隐藏生成图片的img标签和原始文生图标签，避免影响AI回复，节省tokens。
在全局正则中添加以下两个正则：

`/<\s*gen_image\s*>([^<]*)<\/\s*gen_image\s*>(?!.*<\s*gen_image)/i`

gen_image改成你自定义的触发标签。最小深度可以设置为2，为AI保留前一个回复的触发标签。

`<img\b[^>]*?\ssrc\s*=\s*(?:"([^"]*)"|'([^']*)'|([^'"\s>]+))[^>]*?\salt\s*=\s*(?:"([^"]*)"|'([^']*)'|([^'"\s>]+))[^>]*?>`

删除所有img标签，避免AI学习。注意不要打开修改聊天显示的开关，否则自己也看不到图片了。

<img src="./images/help/noGentag.jpg" width="200"/><img src="./images/help/noImgtag.jpg" width="200"/>

9.公网使用

使用frp工具将sd服务暴露到公网，或者使用zerotier等内网穿透工具将你的手机和电脑连接到同一局域网。
