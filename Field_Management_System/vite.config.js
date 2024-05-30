import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import AutoImport from 'unplugin-auto-import/vite';
import path from 'path'; // 静态导入 path 模块

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    AutoImport({
      imports: ['vue', 'vue-router']
    })
  ],
  resolve: {
    extensions: ['.js','.json','.vue'],  //自动添加文件名后缀
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url)),
      '@c': fileURLToPath(new URL('./src/components', import.meta.url)),
      '@v': fileURLToPath(new URL('./src/views', import.meta.url)),

    }
  },
  lintOnSave: false
})
