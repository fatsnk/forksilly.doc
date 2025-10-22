// Reexport the native module. On web, it will be resolved to FileprocessModule.web.ts
// and on native platforms to FileprocessModule.ts
export { default } from './src/FileprocessModule';
export { default as FileprocessView } from './src/FileprocessView';
export * from  './src/Fileprocess.types';
