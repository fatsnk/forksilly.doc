import { registerWebModule, NativeModule } from 'expo';

import { ChangeEventPayload } from './Fileprocess.types';

type FileprocessModuleEvents = {
  onChange: (params: ChangeEventPayload) => void;
}

class FileprocessModule extends NativeModule<FileprocessModuleEvents> {
  PI = Math.PI;
  async setValueAsync(value: string): Promise<void> {
    this.emit('onChange', { value });
  }
  hello() {
    return 'Hello world! 👋';
  }
};

export default registerWebModule(FileprocessModule, 'FileprocessModule');
