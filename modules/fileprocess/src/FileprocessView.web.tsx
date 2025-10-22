import * as React from 'react';

import { FileprocessViewProps } from './Fileprocess.types';

export default function FileprocessView(props: FileprocessViewProps) {
  return (
    <div>
      <iframe
        style={{ flex: 1 }}
        src={props.url}
        onLoad={() => props.onLoad({ nativeEvent: { url: props.url } })}
      />
    </div>
  );
}
