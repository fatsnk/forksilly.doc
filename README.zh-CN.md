# forksilly 应用介绍

forksilly 是一款面向 Android 设备的 AI 聊天应用，在兼容 SillyTavern 常用功能的基础上，扩展了 API 支持、图像生成与工具调用等功能，旨在为用户提供一个可自定义且功能完整的移动端 AI 交互环境。

帮助链接：[![moenew](https://img.shields.io/badge/文档-帮助-blue.svg)](https://github.com/fatsnk/forksilly.doc/blob/main/moenew.md)
[![文生图](https://img.shields.io/badge/文档-图片生成-blue.svg)](https://github.com/fatsnk/forksilly.doc/blob/main/text2img.md)
[![知识库](https://img.shields.io/badge/文档-记忆与知识库-blue.svg)](https://github.com/fatsnk/forksilly.doc/blob/main/Embedding.md)
[![API模板](https://img.shields.io/badge/文档-模板引擎-blue.svg)](https://github.com/fatsnk/APItemplateV2?tab=readme-ov-file)

## 主要功能

### 聊天与内容渲染
- 支持 SillyTavern V2 格式的角色卡、世界书、正则表达式、聊天记录与提示词预设。
- 支持数据导入和导出。
- 渲染 Markdown 内容，包括图片、表格及部分 HTML 标签。如有必要，也可以手动切换为使用webview渲染。
- 在聊天中可发送本地图片。

### API支持与模板引擎
- 内置强大的 API 模板引擎，通过 JSON 配置文件描述任意 LLM API 的请求与响应结构。
- 自动处理 URL 拼接、请求头构建、请求体填充、采样参数映射、图片嵌入及流式响应解析。
- 支持 OpenAI、Claude、Gemini 等常见接口，也可通过导入自定义模板适配其他供应商或私有化部署的 API（请访问[API模板仓库](https://github.com/fatsnk/APItemplateV2?tab=readme-ov-file)获取使用说明和示例模板）。
- 支持在聊天界面随时切换 API 配置与模型。

### 图像生成集成
- 在聊天中可调用文生图工具，由 LLM 直接生成图片并插入对话。
- 支持连接 Stable Diffusion（A1111）API。
- 集成免费图像生成服务 pollinations.ai。
- 提供独立的文生图操作界面，可作为移动端 Stable Diffusion 客户端使用。

### 工具系统与 MCP 服务
- 内置内容追加和内容改写工具，可供 LLM 在对话中调用其他模型处理当次生成的内容。
- 支持连接Streamabe http协议的MCP服务，自动获取可用工具列表并注册到工具系统。
- 工具调用不依赖 LLM 模型自身的函数调用能力，大模型可在聊天中随时调用已注册的工具（详情参考应用工具界面的使用说明）。

### 记忆与知识库
- 基于向量数据库的记忆与知识库系统。
- 支持世界书的向量化存储。

## 使用场景
- 在移动设备上进行沉浸式的角色对话与创作。
- 快速测试和切换不同模型与 API 服务。
- 将聊天与图像生成流程结合，实现多模态内容创作。
- 通过 MCP 服务扩展应用能力，连接外部数据源或工具。

## 注意事项
- 使用 API 或图像生成服务时，需自行准备相应的账户与配置。
- 部分功能如 MCP 服务连接需服务端支持相应协议。
- 调用工具和文生图时，建议关闭API配置中的思维链显示，避免误触发。
