# ForkSilly 项目结构文档

## 概述

ForkSilly 是一个基于 React Native (Expo) 构建的移动端聊天应用，旨在提供一个可定制的、与 AI 模型交互的界面，并兼容 SillyTavern 的部分数据格式。

## forksilly项目结构与文件说明
项目使用自定义导航

### 根目录

*   `.gitignore`: 定义 Git 版本控制忽略的文件和目录（并未使用，项目未使用GitHub管理）。
*   `app.json`: Expo 应用配置文件，包含应用名称、版本、图标、启动画面、平台特定配置等元数据。
*   `App.tsx`: **应用主入口文件**。设置导航结构，集成各个屏幕。**更新：通过引入 `ModalProvider` 和一个 `GlobalModals` 组件，将所有全局模态框（如聊天历史、设置、预览等）的渲染和状态管理提升至顶层，解决了 `z-index` 冲突并统一了动画体验。**集成了 `ChatProvider` 以提供全局聊天状态管理，并集成了 `ThemeProvider` 以提供全局主题和样式管理。
*   `index.ts`: React Native 应用注册入口点。包含重要的polyfill导入，如`react-native-get-random-values`，以提供全局`crypto.getRandomValues`实现。
*   `package.json`: Node.js 项目清单文件。定义项目依赖、脚本（如启动、构建）和基本信息。**（注：近期为实现复制到剪贴板功能，添加了 `expo-clipboard` 依赖。）**
*   `package-lock.json`: 精确锁定项目依赖的版本，确保环境一致性。
*   `tsconfig.json`: TypeScript 编译器配置文件。定义编译选项、文件包含/排除规则，以及重要的**路径别名**（如 `@/components`）。
*   `android/`: 包含 Android 平台的原生项目代码和配置。
*   `assets/`: 存放应用的静态资源，如应用图标 (`icon.png`)、启动画面 (`splash-icon.png`)、默认头像 (`default-avatar.png`) 等。

### `src/` - 源代码目录

存放应用的核心逻辑和界面代码。

*   **`assets/`**:
    *   `default-avatar.png`: 聊天界面中使用的默认头像图片。
*   **`components/`**: 可重用的 UI 组件。
    *   `ChatDialog.tsx`: **聊天对话气泡组件**。负责展示单条聊天消息（用户或 AI），处理不同的消息状态（如加载中、错误）。**更新：流式响应的“打字机”渲染效果已通过新的 `useStreamAnimator` Hook 进行重构，以单一动画循环处理数据流，解决了渲染冲突问题，实现了更平滑、可靠的动画效果。** **集成了主题设置，可以动态调整消息字体和字号，并支持“卡片式主题”（AI回复消息在固定高度可滚动容器内显示）。** **更新：`Message` 类型导入路径已更新为 `../types/message`。** **为首条AI问候语（如果存在可替换项）添加了左右切换按钮，允许用户在 `first_mes` 和 `alternate_greetings` 之间切换。** **更新：现在会从 `ThemeContext` 获取自定义标签规则，并在渲染消息前调用 `customTagService.applyCustomTags` 处理消息文本中的自定义HTML标签。**
    *   `CharacterBubbleSelector.tsx`: **(新增) 角色气泡选择器组件**。提供一个全屏的、带有动画效果的角色选择界面。当触发时，会从屏幕上的一个点“吹出”多个角色气泡，每个气泡代表一个可选角色。气泡会以动画形式移动到屏幕的随机位置，并带有轻微的漂浮和旋转效果。**优化：通过智能分布算法，确保生成的气泡目标位置不会相互重叠，并且会避开顶部的状态栏和底部的输入框区域，以提供清晰的视觉效果。**
    *   `SimpleBrowserModal.tsx`: 使用webview的简单浏览器。
    *   `AnimatedChatHistoryModal.tsx`: **(重构) 聊天历史记录模态框**。
    *   `AnimatedScreenView.tsx`: 提供屏幕导航时的过渡动画。
    *   `ChatInput.tsx`: **聊天输入框组件**。提供文本输入区域、发送按钮、创建新对话按钮。**简化状态管理，统一使用 `isActionMenuVisible` 单一状态控制菜单。**
    *   `EditMessageModal.tsx`: **编辑消息模态框**。
    *   `PresetEntriesToggleModal.tsx`: **预设条目开关模态框**。修复：重构了 JSX 结构，将关闭事件仅绑定在背景遮罩层，并为 `FlatList` 的容器添加 `pointerEvents="box-none"`，解决了列表滚动与模态框关闭手势冲突导致的意外关闭和崩溃问题。**
    *   `SideMenu.tsx`: **侧边栏菜单组件**。提供导航到不同屏幕（如设置、角色管理、全局世界书管理、**主题与样式设置**、**全局正则脚本管理**）的入口。**其导航功能通过消费独立的 `NavigationContext` 实现。**
    *   `TopBar.tsx`: **顶部导航栏组件**。显示当前聊天对象信息（或应用标题），包含打开侧边栏的按钮和其他操作按钮（如编辑、删除消息的触发点、提示词预览、触发更多设置模态框。
    *   `MoreSettingsModal.tsx`: **更多设置模态框**。
    *   `SaveAsModal.tsx`: **另存为对话框**。现已迁移至全局模态框系统，使用 `useAnimatedModal` Hook 实现标准动画效果，并通过 `ModalContext` 进行显示/隐藏控制。
    *   `PromptPreviewModal.tsx`: **提示词预览模态框**。优化：全屏显示，实现了懒加载。触发时会先显示一个加载指示器，然后在后台异步读取文件内容，加载完成后再更新模态框内容。
    *   `ImagePreviewModal.tsx`: **(重构) 图片预览模态框**。现已迁移至全局模态框系统，使用 `useAnimatedModal` Hook 实现标准动画效果。功能保持不变，支持按钮控制的切换、缩放、平移。
    *   `CharacterDetailModal.tsx`: **(新增) 角色详情模态框**。用于显示角色的信息。
    *   `MemoryModal.tsx`: 角色记忆模态框，管理当前角色的记忆。
*   **`context/`**: React Context API 相关文件。
    *   `ModalContext.tsx`: **(新增) 全局模态框上下文**。定义并提供 `ModalProvider`，用于在应用顶层集中管理所有全局模态框的状态。它维护着当前哪个模态框可见 (`visibleModal`) 以及该模态框所需的全部 `props` (`modalProps`)，并提供 `showModal(modal, props)` 和 `hideModal()` 方法供应用内任何组件调用，以实现全局、统一的模态框控制。
    *   `ChatContext.tsx`: **聊天状态上下文**。定义并提供全局聊天状态（如消息列表、当前选择的角色、激活的预设、当前聊天文件等），用于在不同屏幕间持久化聊天状态。**更新：`Message` 类型导入路径已更新为 `../types/message`。** **新增：添加了 `updateActivePresetOrderEntry` 函数，用于处理从 `PresetEntriesToggleModal` 发起的对预设中提示词条目启用状态的更新，确保更改反映在 `activePreset.prompt_order` 和 `activePreset.rawData.prompt_order` 中。注意：虽然 `ChatContext` 本身未直接添加文生图特定状态，但其管理的 `messages` 数组中的每个 `Message` 对象现在可以包含 `imageGenerationRequest` 字段，从而间接持有文生图相关数据。**
    *   `ThemeContext.tsx`: **主题状态上下文**。定义并提供全局主题设置（字体家族、字体大小、活动主题），并负责从 `AsyncStorage` 加载和保存设置，确保主题持久化。**更新：现在也负责加载和提供用户定义的自定义HTML标签渲染规则 (`customTagRules`) 及其加载状态 (`isLoadingCustomTags`)。**
*   **`constants/`**: 存放应用中使用的常量值、样式工具等。
    *   `webViewStyles.ts`: **WebView 样式工具模块**。提供用于 ChatDialog 中 HTML 内容渲染的样式工具函数。包含 `getWebViewHeadContent`（为非独立HTML片段生成完整的head内容，包括移动端视口设置、主题颜色、透明背景和隐藏滚动条样式）和 `getInjectedCss`（为完整HTML文档生成CSS注入字符串以匹配应用主题）两个核心函数，确保 WebView 中渲染的内容与应用主题保持一致。
*   **`core/`**: (暂时为空目录) 可能用于存放应用的核心业务逻辑或框架性代码。
*   **`hooks/`**: 存放自定义 React Hooks，封装可重用的状态逻辑。
    *   `useAnimatedModal.ts`: **(新增) 动画模态框 Hook**。为全局模态框提供统一的动画逻辑。它接收一个 `visible` 布尔值，并使用 `react-native-reanimated` 的 `useSharedValue`、`withTiming` 和 `withSpring`，返回可直接应用于视图的动画样式（`backdropAnimatedStyle` 用于背景遮罩，`modalAnimatedStyle` 用于模态框内容），实现了平滑的淡入/缩放动画效果。
    *   `useMessageActions.ts`: **(重构) 消息操作 Hook**。封装了与聊天消息相关的操作逻辑。**更新：此 Hook 已重构，不再管理本地模态框状态。对于需要弹出模态框的操作（如编辑消息），它现在直接调用 `useModal()` 上下文中的 `showModal('editMessage', ...)` 方法来触发全局模态框。**
    *   `useSlideUpModal.ts`: **(新增) 向上弹出模态框动画 Hook**。为预设条目开关等模态框提供从底部向上滑入的动画效果。使用 `react-native-reanimated` 的 `useSharedValue`、`withTiming` 和 `withSpring` 实现平滑的向上滑动动画，支持背景遮罩的透明度变化和模态框的垂直位移动画，适用于需要从屏幕底部弹出的模态框组件。
    *   `useStreamAnimator.ts`: **(修复) 流式动画 Hook**。支持自适应速度调整。
    *   `useBrowserStorage.ts`: **浏览器存储 Hook**。提供一些内置浏览器使用的函数。
    *   `useSlashCommands.ts`: **(新增) 斜杠命令 Hook**。用于处理聊天输入框中的斜杠命令。
    *   `useChatScreenHandlers.ts`: **(新增) 聊天屏幕处理器 Hook**。包含 `handlePreviewPrompt` 和 `handleMemoryExtractionAndSaving` 等辅助函数。
*   **`navigation/`**: 应用的导航相关服务。
    *   `NavigationService.ts`: **导航服务模块**。定义并导出 `NavigationContext`，供应用内各组件消费以实现导航功能。此模块的引入旨在解决组件间的循环依赖问题。
*   **`screens/`**: 应用的主要屏幕或页面。
    *   `CharacterManagementScreen.tsx`: **角色管理屏幕**。**优化了加载性能，采用三列网格（类相册）布局展示角色卡。** **重构：长按角色头像预览大图的功能，现已改为调用 `useModal().showModal('imagePreview', ...)` 来触发全局的 `ImagePreviewModal`，不再管理本地模态框状态。**
    *   `CharacterEditScreen.tsx`: **角色编辑屏幕**。提供一个全屏界面，用于编辑角色卡的各项详细信息。包含多个标签页：角色信息（名称、描述、性格等）、世界书（**优化了UI，支持逐条添加、编辑、删除条目，包括关键词、内容、启用状态、插入位置和深度等详细设置，角色和位置选择使用更友好的控件；条目内容现在可以折叠以优化显示空间**）、以及高级设置（系统提示、历史指令、标签、**正则表达式脚本**等）。**“可替换的开幕剧情”编辑区已优化为可独立编辑、添加和删除的列表项。** 编辑完成后，用户可以保存更改或取消。
    *   `ChatScreen.tsx`: **主聊天屏幕**。有chatdialog和topbar、chatinput组件。
    *   `SettingsScreen.tsx`: **设置屏幕**。允许用户配置连接 AI 服务所需的参数，如 API 地址、密钥、模型名称、温度、Top-K、最大生成长度、上下文窗口大小等。提供参数启用/禁用开关和 API 连接测试功能。**“提示词后处理设置”选项卡允许用户选择不同的提示词处理模式（原始、严格、半严格）以及是否合并连续系统消息。** **更新：此屏幕已进行重大重构以支持多API配置管理**。用户现在可以：查看和选择已保存的API配置列表（通过Picker组件）；添加新的API配置；编辑所选配置的详细信息（名称、URL、密钥、模型、API类型、流式开关、高级参数、提示词后处理模式等）；删除配置；以及将某个配置设为默认。表单内容会根据当前选择的配置动态更新。新增了API类型选择器，为未来支持不同类型的API做好准备。配置数据通过新的 `apiConfigService.ts` 进行持久化存储和管理。
    *   `PersonaManagementScreen.tsx`: **用户管理屏幕**。包括用户名称、头像和一段提示词，与角色管理一样会提供给提示词构建和占位符替换（{{user}}），插入预设中personaDescription所在的位置
    *   `ThemeSettingsScreen.tsx`: **主题与样式设置屏幕**。允许用户自定义聊天界面的字体家族、字体大小，并选择不同的聊天主题（如默认主题、卡片式主题）。**新增“自定义标签”选项卡，允许用户创建和管理自定义HTML标签的渲染规则（例如，将 `<mytag>content</mytag>` 渲染为Markdown、代码块、可折叠区域或隐藏内容），规则通过 `customTagService` 进行持久化存储。**设置会实时保存并应用于聊天界面。
    *   `PresetManagementScreen.tsx`: **预设管理屏幕**。用于列出、创建、删除和编辑预设。**重构：移除了本地的 `SaveAsModal` 渲染和状态管理，改为通过 `useModal().showModal('saveAs', ...)` 触发全局保存模态框。**
    *   `PresetEditScreen.tsx`: **预设编辑屏幕**。提供全面的预设编辑功能，包括模型与参数编辑区域(A)、提示词管理区域(B)和提示词列表区域(C)。支持编辑预设名称和模型参数；管理、添加、编辑和排序提示词；通过拖拽重新排序提示词；启用/禁用单个提示词；支持新增自定义提示词(生成UUID)；将所有更改保存到预设文件中。解决了多个UI问题，包括`VirtualizedList`嵌套`ScrollView`警告、拖拽体验优化和元素显示问题。角色选择使用单选按钮。
    *   `GlobalWorldBookManagementScreen.tsx`: **全局世界书管理屏幕**。用于列出、导入、启用/禁用和删除全局世界书。编辑操作通过导航到 `GlobalWorldBookEditScreen` 进行。**修复了从编辑界面返回时的导航和刷新参数处理问题，提升了导航稳定性。**
    *   `GlobalWorldBookEditScreen.tsx`: **全局世界书编辑屏幕**。提供全面的全局世界书编辑功能，界面和功能参照角色世界书编辑界面。用户可以编辑世界书的元数据（名称、描述）以及详细编辑其条目。条目编辑支持：名称、内容、主要/次要关键词、启用/禁用状态、固定状态、概率、扫描深度、角色（通过模拟单选按钮选择）、位置（通过下拉选择器选择，并用文字标签显示）、注入深度（条件显示）和插入顺序。
    *   `GlobalRegexManagementScreen.tsx`: **全局正则表达式脚本管理屏幕**。用于列出、导入、导出、编辑和删除全局正则表达式脚本。编辑操作通过导航到 `GlobalRegexEditScreen` 进行。
    *   `GlobalRegexEditScreen.tsx`: **全局正则表达式脚本编辑屏幕**。提供全面的脚本编辑功能，包括脚本名称、查找模式、替换字符串、启用/禁用状态、作用范围（Markdown/Prompt）、目标消息（用户/AI）和深度限制等。
    *   `PersonaManagementScreen.tsx`: **用户角色管理屏幕**。允许用户创建、编辑（内联）、删除、导入/导出和设置默认的用户扮演角色（Persona）。每个角色包含名称、头像、描述提示词和扮演身份（System/User/Assistant）。选择的默认角色及其头像会影响聊天界面的用户头像显示和提示词构建。
    *   `StorageManagementScreen.tsx`: **存储管理屏幕**。提供应用内文件管理界面。**重构：文件预览功能（图片和文本）不再使用本地模态框，而是通过 `useModal().showModal(...)` 触发全局的 `ImagePreviewModal` 或 `PromptPreviewModal`。优化：懒加载。触发预览时会先显示一个加载中模态框，后台读取文件成功后再用实际内容更新模态框。**
    *   `TextToImageSettingsScreen.tsx`: **(新增) 文生图设置屏幕**。允许用户配置文生图相关的设置，例如管理文生图API配置、管理文生图预设，以及设置文生图的触发机制（如自定义触发标签、图片插入位置等）。依赖 `imageGenerationPresetService.ts` 和 `imageTriggerService.ts`。
    *   `MemoryAndKnowledgeScreen.tsx`: **(新增) 记忆与知识库设置屏幕**。用于配置记忆和知识库的相关参数。
*   **`services/`**: 封装应用的业务逻辑、数据处理和与外部服务的交互。
    *   `apiConfigService.ts`: 负责管理多个AI聊天API配置的CRUD操作和持久化存储（使用AsyncStorage）。提供获取默认配置、添加、更新、删除配置等功能。
    *   `characterCardService.ts`: **角色卡服务**。负责处理角色卡数据的加载（**`listCharacterCards` 优化为仅返回文件基本信息以提高角色管理界面的加载速度，完整解析在需要时进行**）、解析、**保存（包括将修改写回PNG文件并正确处理CRC和PNG块，并支持基于名称生成唯一文件名）**和管理。支持中文字符的正确编码和解码。**修复了世界书条目 `position` 5 和 6 的保存问题。**
    *   `chatStorage.ts`: **聊天存储服务**。使用 `expo-file-system` 实现聊天记录的本地持久化存储。将聊天消息以 JSONL 格式保存到文件中（文件名格式兼容 SillyTavern），并提供读取历史记录、列出记录文件（**确保按时间倒序排列**）、加载指定对话、**删除聊天文件、下载聊天文件（准备用于分享）、重命名聊天文件**以及**新增聊天记录导入功能 (`importChat`)，支持从用户选择的 `.jsonl` 文件导入聊天数据。**等功能。**更新：消息转换函数 (`convertScreenMessageToChatMessage`, `convertChatMessageToScreenMessage`) 已移除对 `placeholderReplacements` 字段的处理，因为该字段已从消息类型中移除。现在服务直接保存和加载消息文本。**
    *   `customTagService.ts`: **自定义标签服务**。负责管理用户自定义HTML标签渲染规则的CRUD操作（使用 AsyncStorage 持久化）和核心处理逻辑。提供 `applyCustomTags` 函数，用于根据用户定义的规则（如渲染为Markdown、代码块、折叠区域或隐藏）转换文本中的自定义标签（例如 `<mytag>content</mytag>`，支持自动补全不完整的起始标签如 `mytag>`)。
    *   `globalWorldBookService.ts`: **全局世界书服务**。负责管理全局世界书的元数据（存储在 `global_worldbooks_meta.json`）和各个世界书文件（存储在 `worlds/` 目录下）。提供列出、获取单个世界书条目（解析为 `ProcessedWorldBookEntry[]`）、更新元数据、删除世界书、导入新世界书（从用户选择的JSON文件）、保存世界书条目（将 `ProcessedWorldBookEntry[]` 转换回原始格式并写入文件）以及加载所有已启用的全局世界书条目等功能。
    *   `imageGenerationPresetService.ts`: **文生图预设服务**。负责文生图预设的加载、保存、创建、删除和管理。预设包含默认的文生图参数（如采样器、步数、CFG Scale等）和提示词片段（如正面、负面、前缀、后缀）。
    *   `imageTriggerService.ts`: **文生图触发服务**。负责管理用户自定义的文生图触发机制，例如存储和检索用户设置的触发标签（如 `<gen_image>`) 和图片在消息中的插入位置（顶部/底部）。`ChatScreen` 在收到AI回复时会使用此服务来检测触发标签并提取提示词。
    *   `openAIService.ts`: **OpenAI API 服务**。封装了与 OpenAI 兼容 API 的交互逻辑。支持发送**非流式**（一次性获取完整回复）和**流式**（通过 `XMLHttpRequest` 实现 SSE 接收并逐步返回数据块）请求。处理 API 配置参数（根据启用状态动态添加）、错误处理、流式数据的序列化和取消请求等。**更新：在发送请求前，会根据用户在设置中选择的模式，调用 `promptPostProcessorService` 对提示词进行后处理。`OpenAIConfig` 类型已扩展以包含后处理相关的配置项。** **更新：重构为无状态服务，不再自行管理API配置的加载和保存。其核心方法（如发送请求、测试连接、获取模型列表）现在接受一个 `ApiConfig` 对象作为参数，从而根据传入的配置（如URL、密钥、模型、高级参数）进行操作。旧的 `getConfig()` 方法已移除，配置管理由 `apiConfigService.ts` 统一处理。**
    *   `personaService.ts`: **用户角色服务**。负责管理用户扮演的角色信息（包括名称、头像、描述提示词、扮演身份）。提供加载、保存、导入、导出、增删改查以及设置默认用户角色的功能。头像图片存储在应用专属目录。
    *   `placeholderService.ts`: **占位符服务**。负责解析和替换文本中的动态占位符。目前支持：
        *   `{{user}}` (用户角色名), `{{char}}` (AI角色名) 及其别名 `<user>`, `<char>`。
        *   `{{random::A::B...}}` (或 `{{random:A,B...}}`) 用于从列表中随机选取一项，若无参数则返回0-1随机数。
        *   `{{roll::EXPRESSION}}` (或 `{{roll:EXPRESSION}}`) 用于掷骰计算，支持如 `1d6`、`2d10+3` 等表达式。
        *   `{{lastmessage}}`: 当前聊天中的最后一条消息的文本。
        *   `{{lastusermessage}}`: 聊天记录中最后一条用户消息的文本。
        *   `{{lastcharmessage}}`: 聊天记录中最后一条助手（AI角色）消息的文本。
        **更新：`applyPlaceholders` 函数现在接受 `chatHistory` 作为其上下文参数的一部分，以支持 `{{lastmessage}}` 等占位符。该函数现在仅返回包含已处理文本的对象 (`{ processedText: string }`)，不再返回替换记录数组。**
    *   `presetService.ts`: **预设服务**。负责预设文件的读取、解析、保存和管理。支持列出预设、获取预设详情、创建新预设和更新现有预设。
    *   `promptPostProcessorService.ts`: **提示词后处理服务**。负责根据用户在设置中选择的模式（原始、严格、半严格）以及是否合并连续系统消息的选项，对由 `sillyTavernContextBuilder` 构建的提示词数组进行最终处理，然后再发送给API或用于预览。
    *   `regexApplyService.ts`: **正则表达式应用服务**。负责在指定文本上应用一个或多个正则表达式脚本，执行查找和替换操作。处理脚本的启用状态、作用范围和深度限制。
    *   `regexScriptService.ts`: **正则表达式脚本服务**。负责全局正则表达式脚本的加载、保存、增删改查等管理功能。支持从文件导入和导出脚本列表。
    *   `sillyTavernContextBuilder.ts`: **SillyTavern 上下文构建器**。核心服务，负责根据用户选择的角色、激活的预设、聊天历史、**当前用户扮演角色信息**以及**已激活的角色世界书和全局世界书条目**，严格按照预设中的 `prompt_order` 组装最终发送给 AI 模型的提示词数组。能够处理不同类型的提示片段（如系统提示、角色描述、用户角色描述、聊天历史、世界书条目等）在指定位置的插入。**更新：现在使用 `placeholderService` 进行所有占位符替换，包括修正 `{{user}}` 为当前用户角色名称，并支持新的 `{{random::...}}` 和 `{{roll::...}}` 占位符。对于聊天历史中的消息，它将直接使用已存储的（即已经过占位符处理的）文本。**
    *   `stableDiffusionService.ts`: **(新增) Stable Diffusion 服务**。封装了与 Stable Diffusion API（或其他兼容的文生图API）的交互逻辑。负责根据提供的API配置和参数（如提示词、尺寸、步数等）发送请求，并处理返回的图像数据。
    *   `storageManagementService.ts`: **存储管理服务**。封装了应用存储管理的核心逻辑，负责与设备文件系统交互。主要功能包括：
        *   `listDirectoryContents`: 列出指定目录下的文件和文件夹。
        *   `getItemInfo`: 获取单个文件或目录的详细信息。
        *   `deleteItems`: 删除选定的文件或目录。
        *   `goUp`: 实现向上导航一级目录的逻辑。
        *   `importFiles`: 使用 `expo-document-picker` 允许用户选择文件并将其导入到应用指定路径。
        *   `exportItems`: 导出选定的文件或目录。支持单个文件直接通过 `expo-sharing` 分享，或将多个项目/目录使用 `react-native-zip-archive` 压缩成 ZIP 包后再进行分享。
        *   `getCacheDirectoryInfo`: 获取应用缓存目录的大小和文件数量。
        *   `clearCacheDirectory`: 清空应用缓存目录中的所有内容。
        *   依赖库：`expo-file-system`, `expo-document-picker`, `expo-sharing`, `react-native-zip-archive`。
    *   `streamAnimationService.ts`: **(新增) 流式动画服务**。提供发布-订阅模式的流式数据分发服务，用于接收 ChatScreen 传递的流式数据并分发给多个动画器。实现了消息级别的订阅管理，支持 `subscribe`（订阅指定消息ID的数据流）、`unsubscribe`（取消订阅）、`emit`（发送数据块）和 `forceComplete`（强制完成动画）等核心方法，确保流式动画的数据传递和状态同步。
    *   `worldBookService.ts`: **世界书服务**。负责处理角色卡内嵌世界书和全局世界书条目的解析、处理（将原始条目转换为标准化格式 `ProcessedWorldBookEntry`）、激活（根据聊天历史和关键词匹配）以及转换回存储格式 (`RawWorldBookEntry`)。定义了 `RawWorldBookEntry` 和 `ProcessedWorldBookEntry` 类型。**修复了 `position` 字段值为 5 和 6 时在转换过程中的数据丢失问题。**
    *   `dynamicContextService.ts`: **(新增) 动态上下文服务**。负责构建动态插入到上下文中的记忆和知识库。
    *   `embeddingApiService.ts`: **(新增) 嵌入式模型API服务**。用于处理嵌入式模型的API请求。
    *   `embeddingSettingsService.ts`: **(新增) 嵌入式模型设置服务**。管理嵌入式模型API配置以及记忆与知识库的参数。
    *   `imageGenApiConfigService.ts`: **文生图API配置服务**。负责管理文生图API的配置文件。
    *   `knowledgeService.ts`: **(新增) 数据库操作服务**。封装了处理记忆和知识库相关的表的操作。
    *   `presetVariableService.ts`: (新增) 用于预设编辑屏幕的“处理预设变量”按钮。将预设提示词中的“{{setvar}}”等占位符替换为实际的提示词。
*   **`types/`**: TypeScript 类型定义文件。
    *   `apiTypes.ts`: 定义API配置相关的数据结构，如 `ApiConfig` (单个API配置的完整信息，包括URL、密钥、模型、高级参数、API类型等) 和 `ApiType` (API类型枚举，如OpenAI兼容、Gemini等)。
    *   `app.ts`: 应用级别的通用类型定义。
    *   `chat.ts`: 定义聊天相关的核心数据结构，如 `ChatMessage` (单条消息)、`ChatHeader` (聊天元数据)，与 `chatStorage.ts` 中使用的格式对应。**更新：`ChatMessage` 接口已移除 `placeholderReplacements` 字段。**
    *   `customTag.ts`: 定义自定义标签渲染规则相关的数据结构，如 `CustomTagRule` (单个规则的定义，包括标签名、渲染方式、启用状态等) 和 `CustomTagSettings`。
    *   `imageGeneration.ts`: **(新增) 文生图类型定义**。定义了与文生图功能相关的各种TypeScript数据结构，例如API配置 (`ImageGenApiConfig`)、预设 (`ImageGenerationPreset`)、预设中的提示词片段 (`ImagePromptSnippet`)、Stable Diffusion API参数 (`SdTextToImageParams`) 以及图片触发设置 (`ImageTriggerSettings`) 等。
    *   `message.ts`: 定义了 `Message` 接口，该接口原先在 `ChatScreen.tsx` 中定义，用于聊天界面的消息对象和应用内部状态。将其移至此处是为了解决 `ChatScreen.tsx` 和 `SillyTavernContextBuilder.ts` 之间的循环依赖问题。**更新：`Message` 接口已移除 `placeholderReplacements` 字段。新增：`Message` 接口添加了可选的 `imageGenerationRequest` 字段，用于存储与该消息相关的文生图请求的详细信息，包括提示词、状态、图片URI等。**
    *   `persona.ts`: 定义用户角色（Persona）相关的数据结构，如 `PersonaDescription` (角色描述和扮演身份) 和 `PersonasData` (存储用户角色数据的完整结构)。
    *   `png-modules.d.ts`: 为PNG处理相关模块提供类型声明。
    *   `react-native-event-source.d.ts`: 为使用的某个事件源库（可能用于流式处理）提供 TypeScript 类型声明。
    *   `regex.ts`: 定义与正则表达式脚本相关的数据结构，如 `RegexScript` (单个脚本的结构定义，包含ID、名称、查找模式、替换字符串、启用状态、作用范围、目标消息、深度限制等)。
    *   `op-sqlite.d.ts`: **(新增) op-SQLite类型定义文件**。为 op-SQLite 数据库提供 TypeScript 类型声明。
*   **`utils/`**: 包含通用的辅助函数或工具类。
    *   `textUtils.ts`: 字数统计函数，应用于消息编辑框和提示词预览框。


---
#### 🤣写文档好麻烦，不写了直接放表格了（

*Total : 128 files, 34481 codes, 3930 comments, 3609 blanks, all 42020 lines*

|filename|language|code|comment|blank|total|
|:----|:----|:----|:----|:----|:----|
|App.tsx|TypeScript JSX|196|12|16|224|
|android/app/build.gradle|Groovy|97|68|22|187|
|android/app/src/debug/AndroidManifest.xml|XML|5|0|3|8|
|android/app/src/main/AndroidManifest.xml|XML|27|5|1|33|
|android/app/src/main/java/com/anonymous/forksilly/MainActivity.kt|Kotlin|105|37|19|161|
|android/app/src/main/java/com/anonymous/forksilly/MainApplication.kt|Kotlin|43|3|12|58|
|android/app/src/main/res/drawable/ic_launcher_background.xml|XML|6|0|0|6|
|android/app/src/main/res/drawable/rn_edit_text_material.xml|XML|12|23|3|38|
|android/app/src/main/res/mipmap-anydpi-v26/ic_launcher.xml|XML|5|0|0|5|
|android/app/src/main/res/mipmap-anydpi-v26/ic_launcher_round.xml|XML|5|0|0|5|
|android/app/src/main/res/values-night/colors.xml|XML|1|0|0|1|
|android/app/src/main/res/values/colors.xml|XML|6|0|0|6|
|android/app/src/main/res/values/strings.xml|XML|5|0|0|5|
|android/app/src/main/res/values/styles.xml|XML|13|1|0|14|
|android/app/src/main/res/xml/network_security_config.xml|XML|9|0|1|10|
|android/build.gradle|Groovy|30|2|6|38|
|android/gradle.properties|Properties|16|34|15|65|
|android/gradle/wrapper/gradle-wrapper.properties|Properties|7|0|1|8|
|android/gradlew.bat|Batch|41|32|22|95|
|android/settings.gradle|Groovy|33|0|7|40|
|assets/newbook.json|JSON|1|0|0|1|
|assets/newpreset.json|JSON|248|0|0|248|
|babel.config.js|JavaScript|9|1|1|11|
|index.ts|TypeScript|6|4|4|14|
|modules/fileprocess/android/build.gradle|Groovy|35|4|5|44|
|modules/fileprocess/android/src/main/AndroidManifest.xml|XML|2|0|1|3|
|modules/fileprocess/android/src/main/java/expo/modules/fileprocess/FileprocessModule.kt|Kotlin|189|38|43|270|
|modules/fileprocess/android/src/main/java/expo/modules/fileprocess/FileprocessView.kt|Kotlin|21|5|5|31|
|modules/fileprocess/android/src/main/java/expo/modules/fileprocess/ImageUtils.kt|Kotlin|16|0|4|20|
|modules/fileprocess/expo-module.config.json|JSON|9|0|1|10|
|modules/fileprocess/index.ts|TypeScript|3|2|1|6|
|modules/fileprocess/ios/Fileprocess.podspec|Ruby|19|1|4|24|
|modules/fileprocess/ios/FileprocessModule.swift|Swift|26|15|8|49|
|modules/fileprocess/ios/FileprocessView.swift|Swift|30|2|7|39|
|modules/fileprocess/src/Fileprocess.types.ts|TypeScript|15|0|5|20|
|modules/fileprocess/src/FileprocessModule.ts|TypeScript|13|1|4|18|
|modules/fileprocess/src/FileprocessModule.web.ts|TypeScript|15|0|5|20|
|modules/fileprocess/src/FileprocessView.tsx|TypeScript JSX|8|0|4|12|
|modules/fileprocess/src/FileprocessView.web.tsx|TypeScript JSX|13|0|3|16|
|src/components/AnimatedChatHistoryModal.tsx|TypeScript JSX|631|1|38|670|
|src/components/AnimatedScreenView.tsx|TypeScript JSX|28|0|6|34|
|src/components/CharacterBubbleSelector.tsx|TypeScript JSX|364|15|51|430|
|src/components/CharacterDetailModal.tsx|TypeScript JSX|273|5|23|301|
|src/components/ChatDialog.tsx|TypeScript JSX|1,646|232|125|2,003|
|src/components/ChatHistoryModal.tsx|TypeScript JSX|571|19|35|625|
|src/components/ChatInput.tsx|TypeScript JSX|438|27|39|504|
|src/components/DraggableScrollController.tsx|TypeScript JSX|160|27|16|203|
|src/components/EditMessageModal.tsx|TypeScript JSX|239|0|19|258|
|src/components/ImagePreviewModal.tsx|TypeScript JSX|293|4|31|328|
|src/components/MemoryModal.tsx|TypeScript JSX|1,384|28|100|1,512|
|src/components/MoreSettingsModal.tsx|TypeScript JSX|116|1|8|125|
|src/components/PresetEntriesToggleModal.tsx|TypeScript JSX|224|1|12|237|
|src/components/PromptPreviewModal.tsx|TypeScript JSX|290|0|18|308|
|src/components/SaveAsModal.tsx|TypeScript JSX|151|1|9|161|
|src/components/SideMenu.tsx|TypeScript JSX|300|6|27|333|
|src/components/SimpleBrowserModal.tsx|TypeScript JSX|669|5|50|724|
|src/components/TopBar.tsx|TypeScript JSX|223|7|12|242|
|src/components/renderers/TableRenderer.tsx|TypeScript JSX|108|7|21|136|
|src/constants/defaultCharacterAvatar.ts|TypeScript|1|3|1|5|
|src/constants/defaultCharacterCard.ts|TypeScript|42|1|1|44|
|src/constants/disabledPlaceholders.ts|TypeScript|47|0|0|47|
|src/constants/webViewStyles.ts|TypeScript|91|36|6|133|
|src/context/ChatContext.tsx|TypeScript JSX|120|6|14|140|
|src/context/ModalContext.tsx|TypeScript JSX|33|0|9|42|
|src/context/ThemeContext.tsx|TypeScript JSX|314|35|40|389|
|src/hooks/useAnimatedModal.ts|TypeScript|35|0|6|41|
|src/hooks/useBrowserStorage.ts|TypeScript|66|0|12|78|
|src/hooks/useChatScreenHandlers.ts|TypeScript|155|8|19|182|
|src/hooks/useMessageActions.ts|TypeScript|164|8|21|193|
|src/hooks/usePrevious.ts|TypeScript|8|5|4|17|
|src/hooks/useSlashCommands.ts|TypeScript|76|7|16|99|
|src/hooks/useSlideUpModal.ts|TypeScript|36|0|7|43|
|src/hooks/useStreamAnimator.ts|TypeScript|163|14|40|217|
|src/hooks/useWhyDidYouUpdate.ts|TypeScript|23|13|7|43|
|src/navigation/NavigationService.ts|TypeScript|14|1|2|17|
|src/screens/CharacterEditScreen.tsx|TypeScript JSX|1,509|82|97|1,688|
|src/screens/CharacterManagementScreen.tsx|TypeScript JSX|1,368|149|123|1,640|
|src/screens/ChatScreen.tsx|TypeScript JSX|1,949|324|266|2,539|
|src/screens/GlobalRegexEditScreen.tsx|TypeScript JSX|396|23|29|448|
|src/screens/GlobalRegexManagementScreen.tsx|TypeScript JSX|250|25|18|293|
|src/screens/GlobalWorldBookEditScreen.tsx|TypeScript JSX|914|108|57|1,079|
|src/screens/GlobalWorldBookManagementScreen.tsx|TypeScript JSX|433|73|34|540|
|src/screens/MemoryAndKnowledgeScreen.tsx|TypeScript JSX|1,935|25|131|2,091|
|src/screens/PersonaManagementScreen.tsx|TypeScript JSX|645|32|38|715|
|src/screens/PresetEditScreen.tsx|TypeScript JSX|966|105|92|1,163|
|src/screens/PresetManagementScreen.tsx|TypeScript JSX|738|61|63|862|
|src/screens/SettingsScreen.tsx|TypeScript JSX|1,136|76|63|1,275|
|src/screens/StorageManagementScreen.tsx|TypeScript JSX|695|17|49|761|
|src/screens/TextToImageSettingsScreen.tsx|TypeScript JSX|1,407|114|111|1,632|
|src/screens/ThemeSettingsScreen.tsx|TypeScript JSX|1,159|43|51|1,253|
|src/services/apiConfigService.ts|TypeScript|169|24|28|221|
|src/services/characterCardService.ts|TypeScript|774|223|135|1,132|
|src/services/chatStorage.ts|TypeScript|467|57|99|623|
|src/services/customTagService.ts|TypeScript|312|123|28|463|
|src/services/dynamicContextService.ts|TypeScript|377|24|51|452|
|src/services/embeddingApiService.ts|TypeScript|107|1|14|122|
|src/services/embeddingSettingsService.ts|TypeScript|160|6|17|183|
|src/services/globalWorldBookService.ts|TypeScript|466|106|77|649|
|src/services/imageGenApiConfigService.ts|TypeScript|111|10|12|133|
|src/services/imageGenerationPresetService.ts|TypeScript|131|10|18|159|
|src/services/imageTriggerService.ts|TypeScript|52|11|8|71|
|src/services/knowledgeService.ts|TypeScript|1,515|42|218|1,775|
|src/services/openAIService.ts|TypeScript|721|218|113|1,052|
|src/services/personaService.ts|TypeScript|204|31|28|263|
|src/services/placeholderService.ts|TypeScript|109|75|29|213|
|src/services/presetService.ts|TypeScript|302|46|45|393|
|src/services/presetVariableService.ts|TypeScript|78|16|11|105|
|src/services/promptPostProcessorService.ts|TypeScript|92|36|17|145|
|src/services/regexApplyService.ts|TypeScript|37|18|11|66|
|src/services/regexScriptService.ts|TypeScript|285|59|44|388|
|src/services/sillyTavernContextBuilder.ts|TypeScript|334|243|75|652|
|src/services/stableDiffusionService.ts|TypeScript|144|38|21|203|
|src/services/storageManagementService.ts|TypeScript|272|112|48|432|
|src/services/streamAnimationService.ts|TypeScript|48|3|8|59|
|src/services/worldBookService.ts|TypeScript|406|160|81|647|
|src/styles/tableStyles.ts|TypeScript|34|1|1|36|
|src/types/apiTypes.ts|TypeScript|67|9|13|89|
|src/types/app.ts|TypeScript|46|9|9|64|
|src/types/chat.ts|TypeScript|46|7|6|59|
|src/types/customTag.ts|TypeScript|8|26|2|36|
|src/types/imageGeneration.ts|TypeScript|113|15|11|139|
|src/types/message.ts|TypeScript|20|2|1|23|
|src/types/op-sqlite.d.ts|TypeScript|17|0|4|21|
|src/types/persona.ts|TypeScript|25|2|4|31|
|src/types/png-modules.d.ts|TypeScript|33|28|10|71|
|src/types/react-native-event-source.d.ts|TypeScript|22|3|3|28|
|src/types/regex.ts|TypeScript|33|60|3|96|
|src/utils/textUtils.ts|TypeScript|8|6|6|20|

