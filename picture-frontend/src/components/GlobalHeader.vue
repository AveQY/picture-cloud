<template>
  <div id="globalHeader">
    <div class="header-inner">
      <div class="header-left">
        <router-link to="/" class="brand-link">
          <div class="brand">
            <div class="brand-icon">
              <svg width="28" height="28" viewBox="0 0 28 28" fill="none" xmlns="http://www.w3.org/2000/svg">
                <rect width="28" height="28" rx="8" fill="url(#brand-gradient)"/>
                <path d="M7 19L11 13L14 16L17 11L21 19H7Z" fill="white" fill-opacity="0.9"/>
                <circle cx="10" cy="10" r="2" fill="white" fill-opacity="0.9"/>
                <defs>
                  <linearGradient id="brand-gradient" x1="0" y1="0" x2="28" y2="28" gradientUnits="userSpaceOnUse">
                    <stop stop-color="#6366f1"/>
                    <stop offset="1" stop-color="#818cf8"/>
                  </linearGradient>
                </defs>
              </svg>
            </div>
            <div class="brand-text">
              <span class="brand-title">协同云图台</span>
              <span class="brand-desc">智能图库</span>
            </div>
          </div>
        </router-link>
      </div>

      <div class="header-center">
        <div class="nav-items">
          <template v-for="item in items" :key="item.key">
            <a
              v-if="item && item.key"
              class="nav-item"
              :class="{ 'nav-item-active': current[0] === item.key }"
              @click.prevent="doMenuClick(item)"
            >
              <component :is="item.icon" v-if="item.icon" class="nav-item-icon" />
              {{ item.label }}
            </a>
          </template>
        </div>
      </div>

      <div class="header-right">
        <div v-if="loginUserStore.loginUser.id" class="user-area">
          <a-dropdown :trigger="['click']">
            <a class="user-trigger" @click.prevent>
              <a-space>
                <a-avatar
                  :src="loginUserStore.loginUser.userAvatar"
                  :size="32"
                  class="user-avatar"
                >
                  <template #icon>
                    <UserOutlined />
                  </template>
                </a-avatar>
                <span class="user-name">{{ loginUserStore.loginUser.userName ?? '无名' }}</span>
              </a-space>
            </a>
            <template #overlay>
              <a-menu class="user-dropdown-menu">
                <a-menu-item key="my_space">
                  <router-link to="/my_space">
                    <UserOutlined />
                    我的空间
                  </router-link>
                </a-menu-item>
                <a-menu-divider />
                <a-menu-item key="logout" @click="doLogout">
                  <LogoutOutlined />
                  退出登录
                </a-menu-item>
              </a-menu>
            </template>
          </a-dropdown>
        </div>
        <div v-else class="login-area">
          <a-button type="primary" href="/user/login" class="login-btn">登录</a-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { computed, h, ref } from 'vue'
import { HomeOutlined, LogoutOutlined, UserOutlined } from '@ant-design/icons-vue'
import { message } from 'ant-design-vue'
import { useRouter } from 'vue-router'
import { useLoginUserStore } from '@/stores/useLoginUserStore.ts'
import { userLogoutUsingPost } from '@/api/userController.ts'

const loginUserStore = useLoginUserStore()

const originItems = [
  {
    key: '/',
    icon: () => h(HomeOutlined),
    label: '主页',
    title: '主页',
  },
  {
    key: '/add_picture',
    label: '创建图片',
    title: '创建图片',
  },
  {
    key: '/admin/userManage',
    label: '用户管理',
    title: '用户管理',
  },
  {
    key: '/admin/pictureManage',
    label: '图片管理',
    title: '图片管理',
  },
  {
    key: '/admin/spaceManage',
    label: '空间管理',
    title: '空间管理',
  },
]

const filterMenus = (menus = [] as any) => {
  return menus?.filter((menu) => {
    if (menu?.key?.startsWith('/admin')) {
      const loginUser = loginUserStore.loginUser
      if (!loginUser || loginUser.userRole !== 'admin') {
        return false
      }
    }
    return true
  })
}

const items = computed(() => filterMenus(originItems))

const router = useRouter()
const current = ref<string[]>([])
router.afterEach((to) => {
  current.value = [to.path]
})

const doMenuClick = (item) => {
  if (item && item.key) {
    router.push({ path: item.key })
  }
}

const doLogout = async () => {
  const res = await userLogoutUsingPost()
  if (res.data.code === 0) {
    loginUserStore.setLoginUser({ userName: '未登录' })
    message.success('退出登录成功')
    await router.push('/user/login')
  } else {
    message.error('退出登录失败，' + res.data.message)
  }
}
</script>

<style scoped>
#globalHeader {
  width: 100%;
}

.header-inner {
  display: flex;
  align-items: center;
  height: 100%;
  max-width: 1400px;
  margin: 0 auto;
  gap: 16px;
}

/* ---- 左侧品牌 ---- */
.header-left {
  flex-shrink: 0;
}

.brand-link {
  text-decoration: none;
}

.brand {
  display: flex;
  align-items: center;
  gap: 10px;
}

.brand-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.brand-text {
  display: flex;
  flex-direction: column;
  line-height: 1.2;
}

.brand-title {
  font-size: 17px;
  font-weight: 700;
  color: #0f172a;
  letter-spacing: -0.01em;
}

.brand-desc {
  font-size: 11px;
  color: #94a3b8;
  font-weight: 500;
  letter-spacing: 0.05em;
}

/* ---- 中间菜单 ---- */
.header-center {
  flex: 1;
  display: flex;
  justify-content: center;
  overflow: visible;
}

.header-menu {
  border-bottom: none !important;
  background: transparent !important;
}

.header-menu :deep(.ant-menu-item) {
  padding: 0 16px !important;
  border-radius: 8px;
  margin: 0 2px;
  font-weight: 450;
  font-size: 14px;
  transition: all 0.2s ease;
}

.header-menu :deep(.ant-menu-item-selected) {
  font-weight: 600;
}

/* ---- 右侧用户 ---- */
.header-right {
  flex-shrink: 0;
}

.user-area {
  display: flex;
  align-items: center;
}

.user-trigger {
  cursor: pointer;
  padding: 4px 8px;
  border-radius: 10px;
  transition: background 0.2s ease;
}

.user-trigger:hover {
  background: #f1f5f9;
}

.user-avatar {
  border: 2px solid #e2e8f0;
  transition: border-color 0.2s ease;
}

.user-trigger:hover .user-avatar {
  border-color: #6366f1;
}

.user-name {
  font-size: 14px;
  font-weight: 500;
  color: #0f172a;
  max-width: 80px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.login-btn {
  border-radius: 8px !important;
  font-weight: 600;
}

.login-area {
  display: flex;
  align-items: center;
  gap: 8px;
}

.user-dropdown-menu {
  min-width: 160px;
}
/* ---- 导航项 ---- */
.nav-items {
  display: flex;
  align-items: center;
  gap: 2px;
}

.nav-item {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 6px 16px;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 450;
  color: #475569;
  text-decoration: none;
  transition: all 0.2s ease;
  cursor: pointer;
  white-space: nowrap;
}

.nav-item:hover {
  background: #eef2ff;
  color: #6366f1;
}

.nav-item-active {
  background: #eef2ff;
  color: #6366f1;
  font-weight: 600;
}

.nav-item-icon {
  font-size: 15px;
}
</style>






