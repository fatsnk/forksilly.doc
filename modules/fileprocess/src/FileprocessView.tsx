import { requireNativeView } from 'expo';
import * as React from 'react';

import { FileprocessViewProps } from './Fileprocess.types';

const NativeView: React.ComponentType<FileprocessViewProps> =
  requireNativeView('Fileprocess');

export default function FileprocessView(props: FileprocessViewProps) {
  return <NativeView {...props} />;
}
