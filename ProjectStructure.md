# ForkSilly 项目结构文档

## 概述

ForkSilly 是一个基于 React Native (Expo) 构建的移动端聊天应用，旨在提供一个可定制的、与 AI 模型交互的界面，并兼容 SillyTavern 的部分数据格式。

## forksilly项目结构与文件说明
项目使用自定义导航

### 根目录

*   `.gitignore`: 定义 Git 版本控制忽略的文件和目录。
*   `app.json`: Expo 应用配置文件，包含应用名称、版本、图标、启动画面、平台特定配置等元数据。
*   `App.tsx`: **应用主入口文件**。设置导航结构，集成各个屏幕（如聊天、设置、角色管理、**存储管理**），是整个应用的根组件。**通过将导航上下文 (`NavigationContext`) 提取到独立的 `src/navigation/NavigationService.ts` 模块，解决了组件间的循环依赖问题。**集成了 `ChatProvider` 以提供全局聊天状态管理，并集成了 `ThemeProvider` 以提供全局主题和样式管理。**导航函数已使用 `useCallback` 优化以稳定引用。**
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
    *   `ChatDialog.tsx`: **聊天对话气泡组件**。负责展示单条聊天消息（用户或 AI），处理不同的消息状态（如加载中、错误），并支持流式响应的"打字机"渲染效果。**集成了主题设置，可以动态调整消息字体和字号，并支持“卡片式主题”（AI回复消息在固定高度可滚动容器内显示）。** **更新：`Message` 类型导入路径已更新为 `../types/message`。** **为首条AI问候语（如果存在可替换项）添加了左右切换按钮，允许用户在 `first_mes` 和 `alternate_greetings` 之间切换。** **更新：现在会从 `ThemeContext` 获取自定义标签规则，并在渲染消息前调用 `customTagService.applyCustomTags` 处理消息文本中的自定义HTML标签。**
    *   `ChatHistoryModal.tsx`: **(新增) 聊天历史记录模态框组件**。从 `ChatScreen` 分离出来，负责展示聊天历史列表，并提供加载、创建新聊天、删除、下载/分享、重命名以及**导入聊天记录**的功能。
    *   `ChatInput.tsx`: **聊天输入框组件**。提供文本输入区域、发送按钮、创建新对话按钮（通过操作菜单）。实现了输入框高度动态调整、发送时按钮状态切换（发送图标/加载中/取消）、发送后高度重置等功能。**更新：`onSendMessage` prop 类型已更新以支持异步操作。** **新增：在其操作菜单中集成了 `PresetEntriesToggleModal`，允许用户快速切换当前激活预设中提示词条目的启用状态，并通过 `ChatContext` 的 `updateActivePresetOrderEntry` 函数同步这些更改。**
    *   `EditMessageModal.tsx`: **编辑消息模态框组件**。提供一个模态框界面，用于编辑指定消息的文本内容，包含保存和取消操作。
    *   `PresetEntriesToggleModal.tsx`: **(新增) 预设条目开关模态框组件**。允许用户查看当前激活预设中的提示词条目列表（基于 `prompt_order` 排序和启用状态），并快速切换各个条目的启用/禁用状态。更改会通过回调函数通知 `ChatContext`。
    *   `SideMenu.tsx`: **侧边栏菜单组件**。提供导航到不同屏幕（如设置、角色管理、全局世界书管理、**主题与样式设置**、**全局正则脚本管理**）的入口。**其导航功能通过消费独立的 `NavigationContext` 实现。**
    *   `TopBar.tsx`: **顶部导航栏组件**。显示当前聊天对象信息（或应用标题），包含打开侧边栏的按钮和其他操作按钮（如编辑、删除消息的触发点、提示词预览、**触发更多设置模态框（包含存储管理入口）**）。
    *   `MoreSettingsModal.tsx`: **(新增) 更多设置模态框组件**。从 `ChatScreen` 的 `TopBar` 中的齿轮图标触发，提供如“存储管理”等高级功能的入口。
    *   `SaveAsModal.tsx`: **保存对话框组件**。用于在保存预设或其他内容时提供名称输入和确认操作的界面。
    *   `PromptPreviewModal.tsx`: **提示词预览模态框组件**。用于在聊天界面显示当前根据角色、预设和聊天历史构建的完整提示词内容，方便调试。**更新：现在也被 `StorageManagementScreen` 用于预览文本文件内容。**
    *   `ImagePreviewModal.tsx`: **(新增/重大修改) 图片预览模态框组件**。原用于角色管理界面头像预览，现重构为通用的图片预览组件。移除手势控制，改为使用按钮进行操作：支持图片切换（上一张/下一张及页码显示）、缩放（放大/缩小及比例显示）和平移（上/下/左/右移动及居中，仅在图片放大后可用）。通过点击背景或明确的关闭按钮来关闭。
*   **`context/`**: React Context API 相关文件。
    *   `ChatContext.tsx`: **聊天状态上下文**。定义并提供全局聊天状态（如消息列表、当前选择的角色、激活的预设、当前聊天文件等），用于在不同屏幕间持久化聊天状态。**更新：`Message` 类型导入路径已更新为 `../types/message`。** **新增：添加了 `updateActivePresetOrderEntry` 函数，用于处理从 `PresetEntriesToggleModal` 发起的对预设中提示词条目启用状态的更新，确保更改反映在 `activePreset.prompt_order` 和 `activePreset.rawData.prompt_order` 中。注意：虽然 `ChatContext` 本身未直接添加文生图特定状态，但其管理的 `messages` 数组中的每个 `Message` 对象现在可以包含 `imageGenerationRequest` 字段，从而间接持有文生图相关数据。**
    *   `ThemeContext.tsx`: **主题状态上下文**。定义并提供全局主题设置（字体家族、字体大小、活动主题），并负责从 `AsyncStorage` 加载和保存设置，确保主题持久化。**更新：现在也负责加载和提供用户定义的自定义HTML标签渲染规则 (`customTagRules`) 及其加载状态 (`isLoadingCustomTags`)。**
*   **`constants/`**: (暂时为空目录) 可能用于存放应用中使用的常量值（如 API 默认值、样式常量等）。
*   **`core/`**: (暂时为空目录) 可能用于存放应用的核心业务逻辑或框架性代码。
*   **`hooks/`**: 存放自定义 React Hooks，封装可重用的状态逻辑。
    *   `useMessageActions.ts`: **(新增) 消息操作 Hook**。封装了与聊天消息相关的操作逻辑，如编辑消息、复制消息文本、创建对话分支和删除消息。此 Hook 被 `ChatScreen.tsx` 用来管理这些功能的状态和处理函数，以减少主屏幕组件的复杂性。
*   **`navigation/`**: 应用的导航相关服务。
    *   `NavigationService.ts`: **导航服务模块**。定义并导出 `NavigationContext`，供应用内各组件消费以实现导航功能。此模块的引入旨在解决组件间的循环依赖问题。
*   **`screens/`**: 应用的主要屏幕或页面。
    *   `CharacterManagementScreen.tsx`: **角色管理屏幕**。**优化了加载性能，采用三列网格（类相册）布局展示角色卡（显示矩形头像和文件名），在用户点击具体角色卡进行聊天或编辑时才按需解析角色数据。** 用于展示、导入、管理角色卡片。提供角色卡的下载功能（将角色卡保存到设备公共图片目录中），并在完成后显示成功提示和文件位置。编辑操作通过导航到`CharacterEditScreen`进行。**导航到聊天界面时默认传递 `startNewChat: false` 以加载最近聊天记录。** 修复了从编辑界面返回时的刷新和导航问题。**更新：长按角色头像会调用 `ImagePreviewModal` 显示头像大图。**
    *   `CharacterEditScreen.tsx`: **角色编辑屏幕**。提供一个全屏界面，用于编辑角色卡的各项详细信息。包含多个标签页：角色信息（名称、描述、性格等）、世界书（**优化了UI，支持逐条添加、编辑、删除条目，包括关键词、内容、启用状态、插入位置和深度等详细设置，角色和位置选择使用更友好的控件；条目内容现在可以折叠以优化显示空间**）、以及高级设置（系统提示、历史指令、标签、**正则表达式脚本**等）。**“可替换的开幕剧情”编辑区已优化为可独立编辑、添加和删除的列表项。** 编辑完成后，用户可以保存更改或取消。
    *   `ChatScreen.tsx`: **主聊天屏幕**。这是应用的核心界面，集成了 `TopBar`, `ChatDialog`, `ChatInput`, `SideMenu`, `EditMessageModal` 以及重构后的 `ChatHistoryModal`。**使用全局 `ChatContext` 管理核心聊天状态（消息、选定角色、激活预设、当前聊天文件），以确保在屏幕切换时状态能够持久化。** 负责处理用户输入、调用 `openAIService` 发送请求（支持流式和非流式）、接收和渲染 AI 回复。**功能：支持通过模态框编辑用户和AI消息；支持复制消息内容到剪贴板 (依赖 `expo-clipboard`)；支持从任意消息点创建新的聊天分支，并将历史记录复制到新分支，提示用户是否切换。部分消息操作逻辑（如编辑、复制、分支、删除消息）已通过 `src/hooks/useMessageActions.ts` Hook 进行管理。** 调用 `chatStorage` 保存/加载聊天记录（**切换角色时优先加载最近聊天记录，若无则创建新聊天并自动添加角色首条问候语 `first_mes`；首条问候语支持通过按钮切换 `alternate_greetings`**）、管理发送按钮状态、**接收并使用从角色管理界面传递的角色和预设信息**、**集成提示词预览功能**等复杂交互逻辑。**聊天历史记录模态框 (`ChatHistoryModal`) 现在支持删除、下载/分享、重命名和导入聊天文件。** **优化了导航逻辑及 `useEffect` 依赖，修复了与 `navigation.setParams` 相关的潜在无限渲染循环和状态管理问题，提升了角色切换和新聊天创建的稳定性。** **更新：`Message` 类型定义已移至 `src/types/message.ts` 以解决循环依赖。在用户发送消息、接收AI回复以及保存编辑后的消息时，会调用 `placeholderService` 处理文本中的占位符，确保聊天记录中存储的是已替换占位符的文本，不再存储原始占位符或替换记录到消息对象中。** **更新：在发送API请求和预览提示词前，会根据设置调用 `promptPostProcessorService` 对提示词进行后处理。** **更新：现在会从 `apiConfigService.ts` 获取用户设置的默认API配置 (`activeApiConfig` state)，并在发送消息、预览提示词等操作时使用此配置。当API设置在设置屏幕被更改后（通过 `globalSettingsLastUpdated` 或导航参数 `apiSettingsUpdated` 感知），会自动重新加载最新的默认API配置，确保聊天功能使用正确的用户设定。**
    *   `SettingsScreen.tsx`: **设置屏幕**。允许用户配置连接 AI 服务所需的参数，如 API 地址、密钥、模型名称、温度、Top-K、最大生成长度、上下文窗口大小等。提供参数启用/禁用开关和 API 连接测试功能。**“提示词后处理设置”选项卡允许用户选择不同的提示词处理模式（原始、严格、半严格）以及是否合并连续系统消息。** **更新：此屏幕已进行重大重构以支持多API配置管理**。用户现在可以：查看和选择已保存的API配置列表（通过Picker组件）；添加新的API配置；编辑所选配置的详细信息（名称、URL、密钥、模型、API类型、流式开关、高级参数、提示词后处理模式等）；删除配置；以及将某个配置设为默认。表单内容会根据当前选择的配置动态更新。新增了API类型选择器，为未来支持不同类型的API（如Gemini，目前为占位符）做好准备。配置数据通过新的 `apiConfigService.ts` 进行持久化存储和管理。
    *   `PersonaManagementScreen.tsx`: **用户管理屏幕**。包括用户名称、头像和一段提示词，与角色管理一样会提供给提示词构建和占位符替换（{{user}}），插入预设中personaDescription所在的位置
    *   `ThemeSettingsScreen.tsx`: **主题与样式设置屏幕**。允许用户自定义聊天界面的字体家族、字体大小，并选择不同的聊天主题（如默认主题、卡片式主题）。**新增“自定义标签”选项卡，允许用户创建和管理自定义HTML标签的渲染规则（例如，将 `<mytag>content</mytag>` 渲染为Markdown、代码块、可折叠区域或隐藏内容），规则通过 `customTagService` 进行持久化存储。**设置会实时保存并应用于聊天界面。
    *   `PresetManagementScreen.tsx`: **预设管理屏幕**。用于列出、创建、删除和编辑预设。
    *   `PresetEditScreen.tsx`: **预设编辑屏幕**。提供全面的预设编辑功能，包括模型与参数编辑区域(A)、提示词管理区域(B)和提示词列表区域(C)。支持编辑预设名称和模型参数；管理、添加、编辑和排序提示词；通过拖拽重新排序提示词；启用/禁用单个提示词；支持新增自定义提示词(生成UUID)；将所有更改保存到预设文件中。解决了多个UI问题，包括`VirtualizedList`嵌套`ScrollView`警告、拖拽体验优化和元素显示问题。角色选择使用单选按钮。
    *   `GlobalWorldBookManagementScreen.tsx`: **全局世界书管理屏幕**。用于列出、导入、启用/禁用和删除全局世界书。编辑操作通过导航到 `GlobalWorldBookEditScreen` 进行。**修复了从编辑界面返回时的导航和刷新参数处理问题，提升了导航稳定性。**
    *   `GlobalWorldBookEditScreen.tsx`: **全局世界书编辑屏幕**。提供全面的全局世界书编辑功能，界面和功能参照角色世界书编辑界面。用户可以编辑世界书的元数据（名称、描述）以及详细编辑其条目。条目编辑支持：名称、内容、主要/次要关键词、启用/禁用状态、固定状态、概率、扫描深度、角色（通过模拟单选按钮选择）、位置（通过下拉选择器选择，并用文字标签显示）、注入深度（条件显示）和插入顺序。
    *   `GlobalRegexManagementScreen.tsx`: **全局正则表达式脚本管理屏幕**。用于列出、导入、导出、编辑和删除全局正则表达式脚本。编辑操作通过导航到 `GlobalRegexEditScreen` 进行。
    *   `GlobalRegexEditScreen.tsx`: **全局正则表达式脚本编辑屏幕**。提供全面的脚本编辑功能，包括脚本名称、查找模式、替换字符串、启用/禁用状态、作用范围（Markdown/Prompt）、目标消息（用户/AI）和深度限制等。
    *   `PersonaManagementScreen.tsx`: **用户角色管理屏幕**。允许用户创建、编辑（内联）、删除、导入/导出和设置默认的用户扮演角色（Persona）。每个角色包含名称、头像、描述提示词和扮演身份（System/User/Assistant）。选择的默认角色及其头像会影响聊天界面的用户头像显示和提示词构建。
    *   `StorageManagementScreen.tsx`: **(新增) 存储管理屏幕**。提供应用内文件管理界面，允许用户浏览和操作应用专属文档目录 (`FileSystem.documentDirectory`) 及缓存目录 (`FileSystem.cacheDirectory`) 中的内容。主要特性包括：文件和文件夹列表显示、进入子目录、返回上级目录、多选操作（删除、全选/取消全选）、文件导入 (通过 `expo-document-picker`)、文件/目录导出 (单个文件直接分享，多个项目或目录则压缩为 ZIP 包后分享，使用 `react-native-zip-archive`)。对于缓存目录，还提供显示总大小、文件数量以及一键清空缓存的功能。此屏幕通过 `ChatScreen` 中的 `MoreSettingsModal` 进行导航，并依赖 `storageManagementService.ts` 处理所有底层文件操作。包含返回按钮以导航回前一屏幕。**更新：点击文件项会触发预览功能。图片文件使用 `ImagePreviewModal` (支持按钮控制的切换、缩放、平移)；文本文件 (如 .txt, .json, .log 等) 使用 `PromptPreviewModal` 显示内容。长按文件项仍为选择文件。**
    *   `TextToImageSettingsScreen.tsx`: **(新增) 文生图设置屏幕**。允许用户配置文生图相关的设置，例如管理文生图API配置、管理文生图预设，以及设置文生图的触发机制（如自定义触发标签、图片插入位置等）。依赖 `imageGenerationPresetService.ts` 和 `imageTriggerService.ts`。
*   **`services/`**: 封装应用的业务逻辑、数据处理和与外部服务的交互。
    *   `apiConfigService.ts`: 负责管理多个AI聊天API配置的CRUD操作和持久化存储（使用AsyncStorage）。提供获取默认配置、添加、更新、删除配置等功能。
    *   `characterCardService.ts`: **角色卡服务**。负责处理角色卡数据的加载（**`listCharacterCards` 优化为仅返回文件基本信息以提高角色管理界面的加载速度，完整解析在需要时进行**）、解析、**保存（包括将修改写回PNG文件并正确处理CRC和PNG块，并支持基于名称生成唯一文件名）**和管理。支持中文字符的正确编码和解码。**修复了世界书条目 `position` 5 和 6 的保存问题。**
    *   `chatStorage.ts`: **聊天存储服务**。使用 `expo-file-system` 实现聊天记录的本地持久化存储。将聊天消息以 JSONL 格式保存到文件中（文件名格式兼容 SillyTavern），并提供读取历史记录、列出记录文件（**确保按时间倒序排列**）、加载指定对话、**删除聊天文件、下载聊天文件（准备用于分享）、重命名聊天文件**以及**新增聊天记录导入功能 (`importChat`)，支持从用户选择的 `.jsonl` 文件导入聊天数据。**等功能。**更新：消息转换函数 (`convertScreenMessageToChatMessage`, `convertChatMessageToScreenMessage`) 已移除对 `placeholderReplacements` 字段的处理，因为该字段已从消息类型中移除。现在服务直接保存和加载消息文本。**
    *   `customTagService.ts`: **(新增) 自定义标签服务**。负责管理用户自定义HTML标签渲染规则的CRUD操作（使用 AsyncStorage 持久化）和核心处理逻辑。提供 `applyCustomTags` 函数，用于根据用户定义的规则（如渲染为Markdown、代码块、折叠区域或隐藏）转换文本中的自定义标签（例如 `<mytag>content</mytag>`，支持自动补全不完整的起始标签如 `mytag>`)。
    *   `globalWorldBookService.ts`: **全局世界书服务**。负责管理全局世界书的元数据（存储在 `global_worldbooks_meta.json`）和各个世界书文件（存储在 `worlds/` 目录下）。提供列出、获取单个世界书条目（解析为 `ProcessedWorldBookEntry[]`）、更新元数据、删除世界书、导入新世界书（从用户选择的JSON文件）、保存世界书条目（将 `ProcessedWorldBookEntry[]` 转换回原始格式并写入文件）以及加载所有已启用的全局世界书条目等功能。
    *   `imageGenerationPresetService.ts`: **(新增) 文生图预设服务**。负责文生图预设的加载、保存、创建、删除和管理。预设包含默认的文生图参数（如采样器、步数、CFG Scale等）和提示词片段（如正面、负面、前缀、后缀）。
    *   `imageTriggerService.ts`: **(新增) 图片触发服务**。负责管理用户自定义的文生图触发机制，例如存储和检索用户设置的触发标签（如 `<gen_image>`) 和图片在消息中的插入位置（顶部/底部）。`ChatScreen` 在收到AI回复时会使用此服务来检测触发标签并提取提示词。
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
    *   `storageManagementService.ts`: **(新增) 存储管理服务**。封装了应用存储管理的核心逻辑，负责与设备文件系统交互。主要功能包括：
        *   `listDirectoryContents`: 列出指定目录下的文件和文件夹。
        *   `getItemInfo`: 获取单个文件或目录的详细信息。
        *   `deleteItems`: 删除选定的文件或目录。
        *   `goUp`: 实现向上导航一级目录的逻辑。
        *   `importFiles`: 使用 `expo-document-picker` 允许用户选择文件并将其导入到应用指定路径。
        *   `exportItems`: 导出选定的文件或目录。支持单个文件直接通过 `expo-sharing` 分享，或将多个项目/目录使用 `react-native-zip-archive` 压缩成 ZIP 包后再进行分享。
        *   `getCacheDirectoryInfo`: 获取应用缓存目录的大小和文件数量。
        *   `clearCacheDirectory`: 清空应用缓存目录中的所有内容。
        *   依赖库：`expo-file-system`, `expo-document-picker`, `expo-sharing`, `react-native-zip-archive`。
    *   `worldBookService.ts`: **世界书服务**。负责处理角色卡内嵌世界书和全局世界书条目的解析、处理（将原始条目转换为标准化格式 `ProcessedWorldBookEntry`）、激活（根据聊天历史和关键词匹配）以及转换回存储格式 (`RawWorldBookEntry`)。定义了 `RawWorldBookEntry` 和 `ProcessedWorldBookEntry` 类型。**修复了 `position` 字段值为 5 和 6 时在转换过程中的数据丢失问题。**
*   **`types/`**: TypeScript 类型定义文件。
    *   `apiTypes.ts`: 定义API配置相关的数据结构，如 `ApiConfig` (单个API配置的完整信息，包括URL、密钥、模型、高级参数、API类型等) 和 `ApiType` (API类型枚举，如OpenAI兼容、Gemini等)。
    *   `app.ts`: 应用级别的通用类型定义。
    *   `chat.ts`: 定义聊天相关的核心数据结构，如 `ChatMessage` (单条消息)、`ChatHeader` (聊天元数据)，与 `chatStorage.ts` 中使用的格式对应。**更新：`ChatMessage` 接口已移除 `placeholderReplacements` 字段。**
    *   `customTag.ts`: **(新增)** 定义自定义标签渲染规则相关的数据结构，如 `CustomTagRule` (单个规则的定义，包括标签名、渲染方式、启用状态等) 和 `CustomTagSettings`。
    *   `imageGeneration.ts`: **(新增) 文生图类型定义**。定义了与文生图功能相关的各种TypeScript数据结构，例如API配置 (`ImageGenApiConfig`)、预设 (`ImageGenerationPreset`)、预设中的提示词片段 (`ImagePromptSnippet`)、Stable Diffusion API参数 (`SdTextToImageParams`) 以及图片触发设置 (`ImageTriggerSettings`) 等。
    *   `message.ts`: 定义了 `Message` 接口，该接口原先在 `ChatScreen.tsx` 中定义，用于聊天界面的消息对象和应用内部状态。将其移至此处是为了解决 `ChatScreen.tsx` 和 `SillyTavernContextBuilder.ts` 之间的循环依赖问题。**更新：`Message` 接口已移除 `placeholderReplacements` 字段。新增：`Message` 接口添加了可选的 `imageGenerationRequest` 字段，用于存储与该消息相关的文生图请求的详细信息，包括提示词、状态、图片URI等。**
    *   `persona.ts`: 定义用户角色（Persona）相关的数据结构，如 `PersonaDescription` (角色描述和扮演身份) 和 `PersonasData` (存储用户角色数据的完整结构)。
    *   `png-modules.d.ts`: 为PNG处理相关模块提供类型声明。
    *   `react-native-event-source.d.ts`: 为使用的某个事件源库（可能用于流式处理）提供 TypeScript 类型声明。
    *   `regex.ts`: 定义与正则表达式脚本相关的数据结构，如 `RegexScript` (单个脚本的结构定义，包含ID、名称、查找模式、替换字符串、启用状态、作用范围、目标消息、深度限制等)。
*   **`utils/`**: (暂时为空目录) 可能包含通用的辅助函数或工具类。
