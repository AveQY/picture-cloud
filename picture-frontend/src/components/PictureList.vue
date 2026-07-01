<template>
  <div class="picture-list">
    <a-list
      :grid="{ gutter: 20, xs: 1, sm: 2, md: 3, lg: 4, xl: 5, xxl: 6 }"
      :data-source="dataList"
      :loading="loading"
    >
      <template #renderItem="{ item: picture }">
        <a-list-item style="padding: 0">
          <div class="picture-card" @click="doClickPicture(picture)">
            <div class="picture-card-cover">
              <img
                :alt="picture.name"
                :src="picture.thumbnailUrl ?? picture.url"
                class="picture-card-img"
              />
              <div class="picture-card-overlay">
                <div class="overlay-actions" v-if="showOp">
                  <a-tooltip title="以图搜图">
                    <span class="overlay-btn" @click="(e) => doSearch(picture, e)">
                      <SearchOutlined />
                    </span>
                  </a-tooltip>
                  <a-tooltip title="分享">
                    <span class="overlay-btn" @click="(e) => doShare(picture, e)">
                      <ShareAltOutlined />
                    </span>
                  </a-tooltip>
                  <a-tooltip v-if="canEdit" title="编辑">
                    <span class="overlay-btn" @click="(e) => doEdit(picture, e)">
                      <EditOutlined />
                    </span>
                  </a-tooltip>
                  <a-tooltip v-if="canDelete" title="删除">
                    <span class="overlay-btn overlay-btn-danger" @click="(e) => doDelete(picture, e)">
                      <DeleteOutlined />
                    </span>
                  </a-tooltip>
                </div>
              </div>
              <div class="picture-card-badge">
                <span class="badge-category">{{ picture.category ?? '默认' }}</span>
              </div>
            </div>
            <div class="picture-card-body">
              <div class="picture-card-title" :title="picture.name">{{ picture.name }}</div>
              <div class="picture-card-meta">
                <span class="meta-size">{{ formatFileSize(picture.picSize) }}</span>
                <span class="meta-dimension">{{ picture.picWidth }}×{{ picture.picHeight }}</span>
              </div>
              <div class="picture-card-tags" v-if="picture.tags && picture.tags.length > 0">
                <a-tag v-for="tag in picture.tags.slice(0, 3)" :key="tag" class="card-tag">
                  {{ tag }}
                </a-tag>
                <span v-if="picture.tags.length > 3" class="tag-more">+{{ picture.tags.length - 3 }}</span>
              </div>
            </div>
          </div>
        </a-list-item>
      </template>
    </a-list>
    <ShareModal ref="shareModalRef" :link="shareLink" />
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'
import {
  DeleteOutlined,
  EditOutlined,
  SearchOutlined,
  ShareAltOutlined,
} from '@ant-design/icons-vue'
import { deletePictureUsingPost } from '@/api/pictureController.ts'
import { message } from 'ant-design-vue'
import ShareModal from '@/components/ShareModal.vue'
import { ref } from 'vue'

interface Props {
  dataList?: API.PictureVO[]
  loading?: boolean
  showOp?: boolean
  canEdit?: boolean
  canDelete?: boolean
  onReload?: () => void
}

const props = withDefaults(defineProps<Props>(), {
  dataList: () => [],
  loading: false,
  showOp: false,
  canEdit: false,
  canDelete: false,
})

const router = useRouter()

const doClickPicture = (picture: API.PictureVO) => {
  router.push({ path: `/picture/${picture.id}` })
}

const doSearch = (picture, e) => {
  e.stopPropagation()
  window.open(`/search_picture?pictureId=${picture.id}`)
}

const doEdit = (picture, e) => {
  e.stopPropagation()
  router.push({
    path: '/add_picture',
    query: { id: picture.id, spaceId: picture.spaceId },
  })
}

const doDelete = async (picture, e) => {
  e.stopPropagation()
  const id = picture.id
  if (!id) return
  const res = await deletePictureUsingPost({ id })
  if (res.data.code === 0) {
    message.success('删除成功')
    props.onReload?.()
  } else {
    message.error('删除失败')
  }
}

const shareModalRef = ref()
const shareLink = ref<string>()

const doShare = (picture, e) => {
  e.stopPropagation()
  shareLink.value = `${window.location.protocol}//${window.location.host}/picture/${picture.id}`
  if (shareModalRef.value) {
    shareModalRef.value.openModal()
  }
}

const formatFileSize = (size: number | undefined) => {
  if (!size) return ''
  if (size < 1024) return size + 'B'
  if (size < 1024 * 1024) return (size / 1024).toFixed(1) + 'KB'
  return (size / (1024 * 1024)).toFixed(1) + 'MB'
}
</script>

<style scoped>
.picture-list {
  margin: 0 -2px;
}

/* ---- 卡片容器 ---- */
.picture-card {
  background: #ffffff;
  border-radius: 14px;
  overflow: hidden;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  border: 1px solid #f1f5f9;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.03);
  height: 100%;
  display: flex;
  flex-direction: column;
}

.picture-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 24px -8px rgba(0, 0, 0, 0.1), 0 4px 8px -4px rgba(0, 0, 0, 0.04);
  border-color: #e2e8f0;
}

/* ---- 封面区域 ---- */
.picture-card-cover {
  position: relative;
  overflow: hidden;
  aspect-ratio: 4 / 3;
  background: #f1f5f9;
}

.picture-card-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.5s ease;
}

.picture-card:hover .picture-card-img {
  transform: scale(1.05);
}

/* ---- 悬停遮罩 ---- */
.picture-card-overlay {
  position: absolute;
  inset: 0;
  background: linear-gradient(to top, rgba(15, 23, 42, 0.5) 0%, transparent 50%);
  opacity: 0;
  transition: opacity 0.3s ease;
  display: flex;
  align-items: flex-end;
  justify-content: center;
  padding-bottom: 12px;
}

.picture-card:hover .picture-card-overlay {
  opacity: 1;
}

.overlay-actions {
  display: flex;
  gap: 8px;
}

.overlay-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 34px;
  height: 34px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.25);
  backdrop-filter: blur(4px);
  color: #ffffff;
  font-size: 15px;
  transition: all 0.2s ease;
  cursor: pointer;
}

.overlay-btn:hover {
  background: rgba(255, 255, 255, 0.9);
  color: #0f172a;
  transform: scale(1.1);
}

.overlay-btn-danger:hover {
  background: #fef2f2;
  color: #ef4444;
}

/* ---- 分类角标 ---- */
.picture-card-badge {
  position: absolute;
  top: 8px;
  left: 8px;
}

.badge-category {
  display: inline-block;
  padding: 2px 10px;
  background: rgba(0, 0, 0, 0.45);
  backdrop-filter: blur(4px);
  color: #ffffff;
  font-size: 11px;
  font-weight: 500;
  border-radius: 6px;
  letter-spacing: 0.02em;
}

/* ---- 内容区 ---- */
.picture-card-body {
  padding: 12px 14px 14px;
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.picture-card-title {
  font-size: 14px;
  font-weight: 600;
  color: #0f172a;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  line-height: 1.4;
}

.picture-card-meta {
  display: flex;
  gap: 12px;
  font-size: 12px;
  color: #94a3b8;
}

.picture-card-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 4px;
  margin-top: 4px;
}

.card-tag {
  font-size: 11px !important;
  padding: 0 8px !important;
  line-height: 22px !important;
  height: 22px !important;
  border-radius: 4px !important;
  margin: 0 !important;
  background: #f1f5f9 !important;
  border: none !important;
  color: #64748b !important;
}

.tag-more {
  font-size: 11px;
  color: #94a3b8;
  line-height: 22px;
  padding: 0 4px;
}
</style>
