<template>
  <div id="userRegisterPage">
    <div class="auth-wrapper">
      <div class="auth-card">
        <div class="auth-header">
          <div class="auth-icon">
            <svg width="40" height="40" viewBox="0 0 40 40" fill="none" xmlns="http://www.w3.org/2000/svg">
              <rect width="40" height="40" rx="12" fill="url(#register-gradient)"/>
              <path d="M12 20H28M20 12V28" stroke="white" stroke-width="2.5" stroke-linecap="round"/>
              <defs>
                <linearGradient id="register-gradient" x1="0" y1="0" x2="40" y2="40" gradientUnits="userSpaceOnUse">
                  <stop stop-color="#6366f1"/>
                  <stop offset="1" stop-color="#a78bfa"/>
                </linearGradient>
              </defs>
            </svg>
          </div>
          <h2 class="auth-title">创建账号</h2>
          <p class="auth-desc">注册一个协同云图台账号，开始管理你的图片</p>
        </div>
        <a-form :model="formState" autocomplete="off" @finish="handleSubmit" class="auth-form" layout="vertical">
          <a-form-item name="userAccount" :rules="[{ required: true, message: '请输入账号' }]">
            <a-input v-model:value="formState.userAccount" placeholder="请输入账号" size="large">
              <template #prefix><UserOutlined style="color: #94a3b8" /></template>
            </a-input>
          </a-form-item>
          <a-form-item
            name="userPassword"
            :rules="[
              { required: true, message: '请输入密码' },
              { min: 8, message: '密码长度不能小于 8 位' },
            ]"
          >
            <a-input-password v-model:value="formState.userPassword" placeholder="请输入密码" size="large">
              <template #prefix><LockOutlined style="color: #94a3b8" /></template>
            </a-input-password>
          </a-form-item>
          <a-form-item
            name="checkPassword"
            :rules="[
              { required: true, message: '请输入确认密码' },
              { min: 8, message: '确认密码长度不能小于 8 位' },
            ]"
          >
            <a-input-password v-model:value="formState.checkPassword" placeholder="请再次输入密码" size="large">
              <template #prefix><LockOutlined style="color: #94a3b8" /></template>
            </a-input-password>
          </a-form-item>
          <div class="auth-extra">
            <span></span>
            <router-link to="/user/login" class="auth-link">已有账号？去登录</router-link>
          </div>
          <a-form-item>
            <a-button type="primary" html-type="submit" class="auth-submit-btn" size="large" block>注册</a-button>
          </a-form-item>
        </a-form>
      </div>
    </div>
  </div>
</template>
<script lang="ts" setup>
import { reactive } from 'vue'
import { userRegisterUsingPost } from '@/api/userController.ts'
import { message } from 'ant-design-vue'
import router from '@/router'
import { UserOutlined, LockOutlined } from '@ant-design/icons-vue'

const formState = reactive<API.UserRegisterRequest>({
  userAccount: '',
  userPassword: '',
  checkPassword: '',
})

const handleSubmit = async (values: any) => {
  if (values.userPassword !== values.checkPassword) {
    message.error('两次输入的密码不一致')
    return
  }
  const res = await userRegisterUsingPost(values)
  if (res.data.code === 0 && res.data.data) {
    message.success('注册成功')
    router.push({ path: '/user/login', replace: true })
  } else {
    message.error('注册失败，' + res.data.message)
  }
}
</script>

<style scoped>
#userRegisterPage {
  min-height: calc(100vh - 200px);
  display: flex;
  align-items: center;
  justify-content: center;
}

.auth-wrapper {
  width: 100%;
  max-width: 420px;
  padding: 20px;
}

.auth-card {
  background: #ffffff;
  border-radius: 20px;
  padding: 40px 36px;
  box-shadow: 0 20px 60px rgba(99, 102, 241, 0.08), 0 8px 20px rgba(0, 0, 0, 0.04);
  border: 1px solid rgba(226, 232, 240, 0.5);
}

.auth-header {
  text-align: center;
  margin-bottom: 32px;
}

.auth-icon {
  display: inline-flex;
  margin-bottom: 16px;
}

.auth-title {
  font-size: 24px;
  font-weight: 700;
  color: #0f172a;
  margin: 0 0 8px;
  letter-spacing: -0.02em;
}

.auth-desc {
  font-size: 14px;
  color: #94a3b8;
  margin: 0;
}

.auth-form {
  margin-top: 4px;
}

.auth-form :deep(.ant-form-item) {
  margin-bottom: 20px;
}

.auth-form :deep(.ant-input-affix-wrapper) {
  border-radius: 12px !important;
  padding: 8px 16px;
  border: 1.5px solid #e2e8f0;
  transition: all 0.2s ease;
  background: #f8fafc;
}

.auth-form :deep(.ant-input-affix-wrapper:hover),
.auth-form :deep(.ant-input-affix-wrapper-focused) {
  border-color: #6366f1;
  background: #ffffff;
  box-shadow: 0 0 0 3px rgba(99, 102, 241, 0.08);
}

.auth-form :deep(.ant-input) {
  background: transparent !important;
}

.auth-extra {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  font-size: 13px;
}

.auth-link {
  color: #6366f1;
  font-weight: 500;
  transition: color 0.2s;
}

.auth-link:hover {
  color: #4f46e5;
}

.auth-submit-btn {
  height: 48px !important;
  border-radius: 12px !important;
  font-size: 15px;
  font-weight: 600;
  letter-spacing: 0.01em;
  box-shadow: 0 4px 12px rgba(99, 102, 241, 0.25) !important;
  transition: all 0.2s ease !important;
}

.auth-submit-btn:hover {
  transform: translateY(-1px);
  box-shadow: 0 6px 20px rgba(99, 102, 241, 0.35) !important;
}
</style>
