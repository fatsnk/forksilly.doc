import { NativeModule, requireNativeModule } from 'expo';

import { FileprocessModuleEvents } from './Fileprocess.types';

declare class FileprocessModule extends NativeModule<FileprocessModuleEvents> {
  PI: number;
  hello(): string;
  setValueAsync(value: string): Promise<void>;
  showAlert(title: string, message: string): Promise<void>;
 parseCharacterCardPngAsync(filePath: string): Promise<string>;
 saveCharacterCardPngAsync(sourcePath: string, destinationPath: string, characterJson: string): Promise<void>;
 copyFileAsync(sourcePath: string, destinationPath: string): Promise<void>;
}

// This call loads the native module object from the JSI.
export default requireNativeModule<FileprocessModule>('Fileprocess');
