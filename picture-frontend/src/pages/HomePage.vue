<template>
  <div id="homePage">
    <div class="hero-section">
      <div class="hero-bg"></div>
      <div class="hero-content">
        <h1 class="hero-title">发现精彩图片</h1>
        <p class="hero-subtitle">从海量图片中搜索你想要的灵感</p>
        <div class="search-bar">
          <a-input
            v-model:value="searchParams.searchText"
            placeholder="搜索图片名称、简介..."
            size="large"
            class="hero-search" @press-enter="doSearch"
            allow-clear
          >
            <template #prefix><SearchOutlined style="color: #94a3b8" /></template>
            <template #suffix>
              <SearchOutlined style="color: #6366f1; font-size: 16px; cursor: pointer" @click="doSearch" />
            </template>
          </a-input>
        </div>
      </div>
    </div>

    <div class="home-body">
      <div class="filter-section">
        <div class="filter-row">
          <span class="filter-label">分类</span>
          <a-tabs v-model:active-key="selectedCategory" @change="doSearch" class="category-tabs" :tab-bar-gutter="8">
            <a-tab-pane key="all" tab="全部" />
            <a-tab-pane v-for="category in categoryList" :tab="category" :key="category" />
          </a-tabs>
        </div>
        <div class="filter-row">
          <span class="filter-label">标签</span>
          <div class="tag-bar">
            <a-space :size="[8, 8]" wrap>
              <a-checkable-tag
                v-for="(tag, index) in tagList"
                :key="tag"
                v-model:checked="selectedTagList[index]"
                @change="doSearch"
                class="custom-tag"
              >
                {{ tag }}
              </a-checkable-tag>
            </a-space>
          </div>
        </div>
      </div>

      <PictureList :dataList="dataList" :loading="loading" />

      <div class="pagination-bar">
        <a-pagination
          v-model:current="searchParams.current"
          v-model:pageSize="searchParams.pageSize"
          :total="total"
          @change="onPageChange"
          show-size-changer
          :page-size-options="['12', '24', '48', '96']"
        />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import {
  listPictureTagCategoryUsingGet,
  listPictureVoByPageUsingPost,
} from '@/api/pictureController.ts'
import { SearchOutlined } from '@ant-design/icons-vue'
import { message } from 'ant-design-vue'
import PictureList from '@/components/PictureList.vue'

const dataList = ref<API.PictureVO[]>([])
const total = ref(0)
const loading = ref(true)

const searchParams = reactive<API.PictureQueryRequest>({
  current: 1,
  pageSize: 12,
  sortField: 'createTime',
  sortOrder: 'descend',
})

const fetchData = async () => {
  loading.value = true
  const params = {
    ...searchParams,
    tags: [] as string[],
  }
  if (selectedCategory.value !== 'all') {
    params.category = selectedCategory.value
  }
  selectedTagList.value.forEach((useTag, index) => {
    if (useTag) {
      params.tags.push(tagList.value[index])
    }
  })
  const res = await listPictureVoByPageUsingPost(params)
  if (res.data.code === 0 && res.data.data) {
    dataList.value = res.data.data.records ?? []
    total.value = res.data.data.total ?? 0
  } else {
    message.error('获取数据失败，' + res.data.message)
  }
  loading.value = false
}

onMounted(() => {
  fetchData()
})

const onPageChange = (page: number, pageSize: number) => {
  searchParams.current = page
  searchParams.pageSize = pageSize
  fetchData()
}

const doSearch = () => {
  searchParams.current = 1
  fetchData()
}

const categoryList = ref<string[]>([])
const selectedCategory = ref<string>('all')
const tagList = ref<string[]>([])
const selectedTagList = ref<boolean[]>([])

const getTagCategoryOptions = async () => {
  const res = await listPictureTagCategoryUsingGet()
  if (res.data.code === 0 && res.data.data) {
    tagList.value = res.data.data.tagList ?? []
    categoryList.value = res.data.data.categoryList ?? []
  } else {
    message.error('获取标签分类列表失败，' + res.data.message)
  }
}

onMounted(() => {
  getTagCategoryOptions()
})
</script>


<style scoped>
#homePage {
  margin-bottom: 16px;
}

/* ---- Hero 区域 ---- */
.hero-section {
  position: relative;
  text-align: center;
  padding: 48px 24px 40px;
  margin: -28px -32px 32px;
  background: linear-gradient(135deg, #eef2ff 0%, #f5f3ff 50%, #fdf2f8 100%);
  overflow: hidden;
}

.hero-bg {
  position: absolute;
  inset: 0;
  background:
    radial-gradient(ellipse at 20% 50%, rgba(99, 102, 241, 0.08) 0%, transparent 60%),
    radial-gradient(ellipse at 80% 50%, rgba(244, 63, 94, 0.06) 0%, transparent 60%);
  pointer-events: none;
}

.hero-content {
  position: relative;
  z-index: 1;
}

.hero-title {
  font-size: 32px;
  font-weight: 800;
  color: #0f172a;
  margin: 0 0 8px;
  letter-spacing: -0.03em;
}

.hero-subtitle {
  font-size: 15px;
  color: #64748b;
  margin: 0 0 28px;
}

.hero-search {
  max-width: 520px;
  margin: 0 auto;
}

.hero-search :deep(.ant-input-affix-wrapper) {
  border-radius: 12px !important;
  padding: 4px 20px !important;
  border: 2px solid #e2e8f0 !important;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.04);
  background: #ffffff;
  transition: all 0.2s ease;
}

.hero-search :deep(.ant-input-affix-wrapper:hover),
.hero-search :deep(.ant-input-affix-wrapper-focused) {
  border-color: #6366f1 !important;
  box-shadow: 0 4px 16px rgba(99, 102, 241, 0.12) !important;
}

.hero-search :deep(.ant-input) {
  font-size: 15px;
}

/* ---- 筛选区域 ---- */
.home-body {
  animation: fadeInUp 0.5s ease-out both;
}

.filter-section {
  background: #ffffff;
  border-radius: 16px;
  padding: 20px 24px;
  margin-bottom: 24px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.03);
  border: 1px solid #f1f5f9;
}

.filter-row {
  display: flex;
  align-items: flex-start;
  gap: 16px;
}

.filter-row + .filter-row {
  margin-top: 12px;
  padding-top: 12px;
  border-top: 1px solid #f1f5f9;
}

.filter-label {
  flex-shrink: 0;
  font-size: 13px;
  font-weight: 600;
  color: #64748b;
  padding-top: 8px;
  min-width: 36px;
}

.category-tabs {
  flex: 1;
}

.category-tabs :deep(.ant-tabs-nav) {
  margin-bottom: 0 !important;
}

.category-tabs :deep(.ant-tabs-nav::before) {
  border-bottom: none !important;
}

.category-tabs :deep(.ant-tabs-tab) {
  padding: 4px 16px !important;
  font-size: 13px;
  border-radius: 8px !important;
  transition: all 0.2s ease !important;
}

.category-tabs :deep(.ant-tabs-tab-active) {
  background: #eef2ff !important;
}

.category-tabs :deep(.ant-tabs-ink-bar) {
  display: none;
}

.tag-bar {
  flex: 1;
  padding-top: 4px;
}

.custom-tag {
  padding: 4px 14px !important;
  border-radius: 8px !important;
  font-size: 13px !important;
  border: 1.5px solid #e2e8f0 !important;
  background: #ffffff !important;
  color: #475569 !important;
  transition: all 0.2s ease !important;
  cursor: pointer;
}

.custom-tag:hover {
  border-color: #6366f1 !important;
  color: #6366f1 !important;
}

.custom-tag.ant-tag-checkable-checked {
  background: #eef2ff !important;
  border-color: #6366f1 !important;
  color: #4f46e5 !important;
  font-weight: 500;
}

/* ---- 分页 ---- */
.pagination-bar {
  display: flex;
  justify-content: flex-end;
  margin-top: 24px;
  padding-top: 16px;
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










