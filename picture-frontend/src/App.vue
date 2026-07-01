<template>
  <div id="app">
    <a-config-provider
      :locale="zhCN"
      :theme="{
        token: {
          colorPrimary: '#6366f1',
          colorPrimaryHover: '#818cf8',
          colorPrimaryActive: '#4f46e5',
          borderRadius: 8,
          colorBgContainer: '#ffffff',
          colorBgElevated: '#ffffff',
          colorBorderSecondary: '#e2e8f0',
          colorText: '#0f172a',
          colorTextSecondary: '#475569',
          colorTextTertiary: '#94a3b8',
          colorBgLayout: '#f5f3ff',
          fontFamily: 'Inter, -apple-system, BlinkMacSystemFont, Segoe UI, Roboto, Helvetica Neue, Arial, Noto Sans, sans-serif',
          boxShadow: '0 1px 2px 0 rgba(0,0,0,0.03)',
        },
        components: {
          Menu: {
            itemBorderRadius: 8,
            itemColor: '#475569',
            itemHoverColor: '#6366f1',
            itemHoverBg: '#eef2ff',
            itemSelectedColor: '#6366f1',
            itemSelectedBg: '#eef2ff',
            horizontalItemSelectedColor: '#6366f1',
            dropdownItemBorderRadius: 8,
          },
          Button: {
            borderRadius: 8,
            controlHeight: 40,
            primaryShadow: '0 2px 4px rgba(99,102,241,0.2)',
          },
          Card: {
            borderRadius: 12,
          },
          Input: {
            borderRadius: 8,
            controlHeight: 44,
          },
          Tabs: {
            borderRadius: 8,
          },
          Tag: {
            borderRadius: 6,
          },
          Modal: {
            borderRadius: 16,
          },
          Dropdown: {
            borderRadius: 12,
          },
        },
      }"
    >
      <BasicLayout />
    </a-config-provider>
  </div>
</template>

<script setup lang="ts">
import zhCN from 'ant-design-vue/es/locale/zh_CN';
import dayjs from 'dayjs';
import 'dayjs/locale/zh-cn';
dayjs.locale('zh-cn');

import BasicLayout from '@/layouts/BasicLayout.vue'
</script>
