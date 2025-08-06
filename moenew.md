# 新手帮助文档：

第一次接触AIRP，不知道什么是kobold、sillytavern的玩家，参考下面的简单使用介绍。
---

## 🎬 新手快速上手指南

### 1. 打开app & 创建 Persona

* 假设你已经安装好ForkSilly并正常开启应用。
* 此时界面应该非常简洁。直接点击左上角≡弹出侧边菜单，点击“you”，输入你自己的用户设定，然后选择头像并保存。聊天时，该设定会告诉AI你是谁。

---

### 2. 配置 API 连接

* 点击侧边菜单中的 **API设置** 。
* 两种常见方式：

  **A. 使用免费模型**

  * 可以前往gemini官网[ai.dev](https://ai.dev)，申请免费的api key。获取key后，在API设置选择gemini类型，填写你的key，然后点击测试连接。
  * 或注册其它送免费额度的提供商，在 "API设置" 选择 OpenAI兼容类型，粘贴 Key 并点击“测试连接”验证连接是否有效。

  **B. 使用付费模型**

  可以使用OpenAI兼容格式的URL。例如：
  * ChatGPT：在 [OpenAI 官网](https://platform.openai.com)生成 API Key。
  * deepseek：[在deepseek官网](https://platform.deepseek.com/sign_in) 获取API key, 并使用官方的API url: `https://api.deepseek.com/v1`
  * 在 "API设置" 选择 OpenAI兼容类型，粘贴 api Key到“API密钥” 并点击“测试连接”验证连接是否有效。

* 配置完毕后，点击“保存设置”关闭设置窗口。

---

### 3. 获取角色（Character）

* 前往你知道的地方获取角色卡，比如Discord社区，或者直接在Google搜索`sillytavern Character card`，第一页会有几个分享角色卡的网站，前往下载png格式的角色卡。
  * 或者自行创建角色：[OcO萌/AI角色卡编辑器：CharacterEditor](https://ce.ooc.moe/zh-CN)
* 点击侧边菜单的 **角色列表** 。
* 导入你下载或创建的png角色卡。

---

### 4. 设置预设

* 点击侧边菜单的 **预设列表** 。
* 点击“新建预设”，点击启用。
* 虽然默认设置足够入门，但如果你了解提示词（prompt）设计，可以在这里配置详细的上下文。
* 你可以使用别人分享的sillytavern预设来更好地指导AI如何进行输出；通常，你可以在sillytavern和角色卡相关的社区中找到网友分享的预设。

---

### 5. 发起对话

* 点击侧边菜单的 **角色列表** 。
* 选择一个角色，会自动导航到聊天屏幕。
* 在底部输入框输入你想说的话，然后点击发送。

---

## 文生图

应用支持连接Stable Diffusion进行文生图。

文生图配置教程：[文生图简单教程](text2img.md)

### 6. （进阶）


---

