<template>
  <div id="basicLayout">
    <a-layout class="layout-root">
      <a-layout-header class="layout-header">
        <GlobalHeader />
      </a-layout-header>
      <a-layout>
        <GlobalSider class="layout-sider" />
        <a-layout-content class="layout-content">
          <div class="content-inner">
            <router-view />
          </div>
        </a-layout-content>
      </a-layout>
    </a-layout>
  </div>
</template>

<script setup lang="ts">
import GlobalHeader from '@/components/GlobalHeader.vue'
import GlobalSider from "@/components/GlobalSider.vue";
</script>

<style scoped>
.layout-root {
  min-height: 100vh;
  background: var(--bg-page, #f5f3ff);
}

.layout-header {
  padding-inline: 24px !important;
  background: rgba(255, 255, 255, 0.85) !important;
  backdrop-filter: blur(16px);
  -webkit-backdrop-filter: blur(16px);
  border-bottom: 1px solid rgba(226, 232, 240, 0.6);
  height: var(--header-height, 64px);
  line-height: var(--header-height, 64px);
  position: sticky;
  top: 0;
  z-index: 100;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.03);
}

.layout-sider {
  background: transparent !important;
  border-right: none;
  padding-top: 24px;
  padding-left: 8px;
  position: sticky;
  top: var(--header-height, 64px);
  height: calc(100vh - var(--header-height, 64px));
  overflow-y: auto;
}

.layout-sider :deep(.ant-layout-sider) {
  background: transparent !important;
}

.layout-sider :deep(.ant-menu) {
  background: transparent !important;
  border-inline-end: none !important;
}

.layout-sider :deep(.ant-menu-item) {
  margin: 2px 8px !important;
  border-radius: 10px !important;
  font-weight: 450;
}

.layout-sider :deep(.ant-menu-item-selected) {
  font-weight: 600;
}

.layout-sider :deep(.ant-menu-item-group-title) {
  padding: 12px 16px 4px !important;
  font-size: 11px;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.05em;
  color: #94a3b8 !important;
}

.layout-content {
  padding: 28px 32px 40px;
  min-height: calc(100vh - var(--header-height, 64px));
}

.content-inner {
  max-width: 1400px;
  margin: 0 auto;
  animation: fadeInUp 0.4s ease-out both;
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(12px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>
