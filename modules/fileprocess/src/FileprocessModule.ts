import { NativeModule, requireNativeModule } from 'expo';

import { FileprocessModuleEvents } from './Fileprocess.types';

/**
 * 聊天文件信息接口 - 由原生模块直接返回
 */
export interface ChatFileInfo {
  file_name: string;
  file_size: number;
  last_modified: number;
  message_count: number;
  preview_message: string;
}

declare class FileprocessModule extends NativeModule<FileprocessModuleEvents> {
  PI: number;
  hello(): string;
  setValueAsync(value: string): Promise<void>;
  showAlert(title: string, message: string): Promise<void>;
  parseCharacterCardPngAsync(filePath: string): Promise<string>;
  saveCharacterCardPngAsync(sourcePath: string, destinationPath: string, characterJson: string): Promise<void>;
  copyFileAsync(sourcePath: string, destinationPath: string): Promise<void>;
  convertJpgToPngAsync(inputPath: string, outputPath: string): Promise<void>;
  
  // 聊天文件处理方法（仅 Android）
  /** 读取聊天文件 - 直接返回结构化数组，无需 JSON.parse，利用 JSI 零拷贝传递 */
  readChatFileAsync(filePath: string): Promise<Array<Record<string, any>>>;
  /** 获取聊天文件列表信息 - 直接返回结构化数组，无需 JSON.parse */
  getChatFilesInfoAsync(directoryPath: string): Promise<ChatFileInfo[]>;
  /** 保存聊天记录 - Native 侧自己读取 header，JS 只传递文件路径和消息数组，利用 JSI 零拷贝传递 */
  saveChatAsync(filePath: string, messages: Array<Record<string, any>>): Promise<void>;
  appendMessageToChatAsync(filePath: string, messageJson: string): Promise<void>;
}

// This call loads the native module object from the JSI.
export default requireNativeModule<FileprocessModule>('Fileprocess');
