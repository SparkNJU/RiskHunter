<script setup lang="ts">
import { ref } from 'vue';
import { InfoFilled } from '@element-plus/icons-vue'

// 定义选项卡选择状态
const activeTab = ref('teachers');

// 定义各组成员数据
const teachers = [
    {
        name: '张教授',
        title: '南京大学商学院',
        description: '金融学教授，研究方向为汇率风险管理与国际金融',
        avatar: '/src/assets/teamMembers/arknights.jpg'
    },
    {
        name: '李教授',
        title: '南京大学软件学院',
        description: '人工智能与数据科学教授，专注于金融科技领域',
        avatar: '/src/assets/teamMembers/arknights.jpg'
    },
];

const businessTeam = [
    {
        name: '王琦',
        title: '团队负责人',
        description: '南京大学商学院金融系，擅长金融风险建模与分析',
        avatar: '/src/assets/teamMembers/arknights.jpg'
    },
    {
        name: '陈明',
        title: '数据分析师',
        description: '南京大学商学院，专注于外汇市场分析与预测',
        avatar: '/src/assets/teamMembers/arknights.jpg'
    },
    {
        name: '赵婷',
        title: '商业顾问',
        description: '南京大学商学院市场营销专业，负责市场需求分析',
        avatar: '/src/assets/teamMembers/arknights.jpg'
    },
    {
        name: '周楠',
        title: '金融顾问',
        description: '南京大学商学院金融学，专注金融风险管理研究',
        avatar: '/src/assets/teamMembers/arknights.jpg'
    },
];

const techTeam = [
    {
        name: '张浩',
        title: '技术负责人',
        description: '南京大学软件学院，全栈开发工程师，项目架构师',
        avatar: '/src/assets/teamMembers/arknights.jpg'
    },
    {
        name: '刘伟',
        title: '后端开发',
        description: '南京大学软件学院，专注Python与数据处理技术',
        avatar: '/src/assets/teamMembers/arknights.jpg'
    },
    {
        name: '徐佳',
        title: '前端开发',
        description: '南京大学软件学院，Vue.js专家，负责用户界面开发',
        avatar: '/src/assets/teamMembers/arknights.jpg'
    },
    {
        name: '孙琪',
        title: 'AI工程师',
        description: '南京大学人工智能学院，负责模型训练与优化',
        avatar: '/src/assets/teamMembers/arknights.jpg'
    },
    {
        name: '孙琪',
        title: 'AI工程师',
        description: '南京大学人工智能学院，负责模型训练与优化',
        avatar: '/src/assets/teamMembers/arknights.jpg'
    },
];
</script>

<template>
    <div class="about-us-container">
        <!-- 标题部分保持不变 -->
        <div class="about-header">
            <el-icon>
                <InfoFilled />
            </el-icon>
            <h2 class="about-title">关于我们</h2>
        </div>

        <p class="about-description">
            RiskHunter平台由NJU-SagaCiti团队开发，团队成员来自南京大学商学院、软件学院和人工智能学院，具有深厚的跨学科专业背景、强大的商业分析能力和开发技术。
        </p>

        <div class="team-tabs">
            <div class="tab-selector">
                <div class="tab-item" :class="{ active: activeTab === 'teachers' }" @click="activeTab = 'teachers'">
                    指导教师
                </div>
                <div class="tab-item" :class="{ active: activeTab === 'business' }" @click="activeTab = 'business'">
                    商业组
                </div>
                <div class="tab-item" :class="{ active: activeTab === 'tech' }" @click="activeTab = 'tech'">
                    技术组
                </div>
            </div>

            <!-- 指导教师部分 - 横向布局 -->
            <div class="team-members" v-if="activeTab === 'teachers'">
        <div class="teachers-row">
            <!-- 第一位教师 - 照片在左 -->
            <div class="teacher-card left-image">
                <div class="teacher-avatar">
                    <img :src="teachers[0].avatar" alt="教师头像">
                </div>
                <div class="teacher-info">
                    <h3 class="member-name">{{ teachers[0].name }}</h3>
                    <div class="member-title">{{ teachers[0].title }}</div>
                    <p class="member-description">{{ teachers[0].description }}</p>
                </div>
            </div>

            <!-- 第二位教师 - 照片在右 -->
            <div class="teacher-card right-image">
                <div class="teacher-info">
                    <h3 class="member-name">{{ teachers[1].name }}</h3>
                    <div class="member-title">{{ teachers[1].title }}</div>
                    <p class="member-description">{{ teachers[1].description }}</p>
                </div>
                <div class="teacher-avatar">
                    <img :src="teachers[1].avatar" alt="教师头像">
                </div>
            </div>
        </div>
    </div>

            <!-- 商业组部分 - 一行四个 -->
            <div class="team-members" v-if="activeTab === 'business'">
                <div class="business-row">
                    <div class="business-card" v-for="(member, index) in businessTeam" :key="index">
                        <div class="member-avatar">
                            <img :src="member.avatar" alt="成员头像">
                        </div>
                        <div class="member-info">
                            <h3 class="member-name">{{ member.name }}</h3>
                            <div class="member-title">{{ member.title }}</div>
                            <p class="member-description">{{ member.description }}</p>
                        </div>
                    </div>
                </div>
            </div>

            <!-- 技术组部分 - 一行五个 -->
            <div class="team-members" v-if="activeTab === 'tech'">
                <div class="tech-row">
                    <div class="tech-card" v-for="(member, index) in techTeam" :key="index">
                        <div class="member-avatar">
                            <img :src="member.avatar" alt="成员头像">
                        </div>
                        <div class="member-info">
                            <h3 class="member-name">{{ member.name }}</h3>
                            <div class="member-title">{{ member.title }}</div>
                            <p class="member-description">{{ member.description }}</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<style scoped>
.about-us-container {
    width: 100%;
    max-width: 1200px;
    margin: 0 auto;
    padding: 2rem 1rem;
    text-align: center;
}

/* 新增标题头部容器 */
.about-header {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 12px;
    /* 图标和文字的间距 */
    margin: 1.8rem 0;
    /* 上下边距 */
}

/* 修改标题样式 */
.about-title {
    color: #512da8;
    /* 更深的蓝紫色 */
    font-size: 1.5rem;
    /* 调整字体大小与intro-about一致 */
    font-weight: 500;
    /* 稍微加粗 */
    margin: 0;
    /* 移除默认边距 */
    position: relative;
    /* 保持相对定位用于下划线 */
}

/* 移除原来的下划线效果 */
.about-title::after {
    content: none;
}

/* 为图标设置样式 */
.about-header .el-icon {
    color: #512da8;
    /* 图标颜色与文字一致 */
    font-size: 1.5rem;
    /* 图标大小与文字一致 */
}


.about-description {
    color: #303030;
    font-size: 1.1rem;
    line-height: 1.8;
    margin-bottom: 2.5rem;
    max-width: 800px;
    margin-left: auto;
    margin-right: auto;
    text-align: center;
}

.tab-selector {
    display: flex;
    justify-content: center;
    margin-bottom: 2rem;
    border-bottom: 1px solid #e0e0e0;
}

.tab-item {
    padding: 1rem 2rem;
    cursor: pointer;
    color: #303030;
    font-size: 1.1rem;
    position: relative;
    transition: all 0.3s;
}

.tab-item:hover {
    color: #626aef;
}

.tab-item.active {
    color: #626aef;
    font-weight: 500;
}

.tab-item.active::after {
    content: '';
    position: absolute;
    bottom: -1px;
    left: 0;
    width: 100%;
    height: 3px;
    background-color: #626aef;
}

.team-members {
    margin-top: 2rem;
}

.members-row {
    display: flex;
    flex-wrap: wrap;
    justify-content: center;
    gap: 2rem;
}

.member-card {
    width: calc(50% - 2rem);
    max-width: 350px;
    margin-bottom: 2rem;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
    border-radius: 10px;
    overflow: hidden;
    transition: transform 0.3s, box-shadow 0.3s;
}

.member-card:hover {
    transform: translateY(-5px);
    box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
}

.member-avatar {
    width: 100%;
    height: 200px;
    overflow: hidden;
}

.member-avatar img {
    width: 100%;
    height: 100%;
    object-fit: cover;
    transition: transform 0.3s;
}

.member-card:hover .member-avatar img {
    transform: scale(1.05);
}

.member-info {
    padding: 1.5rem;
    text-align: center;
}

.member-name {
    color: #303030;
    margin: 0 0 0.5rem;
    font-size: 1.3rem;
}

.member-title {
    color: #626aef;
    font-size: 0.9rem;
    margin-bottom: 1rem;
}

.member-description {
    color: #303030;
    font-size: 0.9rem;
    line-height: 1.6;
}

/* 响应式调整 */
@media (max-width: 768px) {
    .members-row {
        flex-direction: column;
        align-items: center;
    }

    .member-card {
        width: 100%;
        max-width: 400px;
    }

    .tab-item {
        padding: 0.8rem 1rem;
        font-size: 1rem;
    }
}
/* 指导教师布局样式 */
.teachers-row {
    display: flex;
    flex-direction: column;
    gap: 2rem;
    margin-bottom: 2rem;
    width: 100%;
}

/* 基础教师卡片样式 */
.teacher-card {
    width: 100%;
    max-width: 1100px;
    margin: 0 auto;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
    border-radius: 10px;
    overflow: hidden;
    transition: transform 0.3s, box-shadow 0.3s;
    height: 180px;
}

/* 左侧图片布局 - 增加选择器特异性 */
.teachers-row .teacher-card.left-image {
    display: flex;
    flex-direction: row;
}

/* 右侧图片布局 - 增加选择器特异性 */
.teachers-row .teacher-card.right-image {
    display: flex;
    flex-direction: row-reverse;
}

.teacher-card:hover {
    transform: translateY(-5px);
    box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
}

.teacher-avatar {
    width: 15%;
    min-width: 120px;
    max-width: 135px;
    overflow: hidden;
    flex-shrink: 0; /* 防止图片区域被压缩 */
}

.teacher-avatar img {
    width: 100%;
    height: 100%;
    object-fit: cover;
    aspect-ratio: 3/4;
}

.teacher-info {
    flex: 1;
    padding: 1.5rem 2.5rem;
    text-align: left;
    display: flex;
    flex-direction: column;
    justify-content: center;
}

/* 商业组和技术组共用的头像样式 - 修改为证件照比例 */
.business-card .member-avatar,
.tech-card .member-avatar {
    width: 100%;
    height: auto;
    overflow: hidden;
    position: relative;
    padding-top: 133.33%; /* 3:4 证件照比例 */
}

.business-card .member-avatar img,
.tech-card .member-avatar img {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    object-fit: cover;
}

/* 重写冲突的响应式样式 */
@media (max-width: 992px) {
    /* 覆盖其他媒体查询中可能导致冲突的样式 */
    .teachers-row .teacher-card {
        height: auto;
    }
    
    /* 明确指定在小屏幕上的布局 */
    .teachers-row .teacher-card.left-image,
    .teachers-row .teacher-card.right-image {
        flex-direction: column;
        max-width: 500px;
    }
    
    .teacher-avatar {
        width: 100%;
        min-width: 100%;
        max-width: 100%;
        height: 150px;
    }
    
    .teacher-avatar img {
        aspect-ratio: 16/9;
    }
    
    .teacher-info {
        text-align: center;
        padding: 1.5rem;
    }
}
/* 商业组布局样式 */
.business-row {
    display: flex;
    flex-wrap: wrap;
    justify-content: center;
    gap: 1.5rem;
}

.business-card {
    width: calc(25% - 1.5rem);
    /* 一行四个 */
    margin-bottom: 1.5rem;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
    border-radius: 10px;
    overflow: hidden;
    transition: transform 0.3s, box-shadow 0.3s;
}

.business-card:hover {
    transform: translateY(-5px);
    box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
}

/* 技术组布局样式 */
.tech-row {
    display: flex;
    flex-wrap: wrap;
    justify-content: center;
    gap: 1rem;
}

.tech-card {
    width: calc(20% - 1rem);
    /* 一行五个 */
    margin-bottom: 1.5rem;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
    border-radius: 10px;
    overflow: hidden;
    transition: transform 0.3s, box-shadow 0.3s;
}

.tech-card:hover {
    transform: translateY(-5px);
    box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
}

/* 商业组和技术组的信息部分样式 */
.business-card .member-info,
.tech-card .member-info {
    padding: 1rem;
}

.business-card .member-name,
.tech-card .member-name {
    font-size: 1.1rem;
    margin: 0 0 0.3rem;
}

.business-card .member-title,
.tech-card .member-title {
    font-size: 0.8rem;
    margin-bottom: 0.5rem;
}

.business-card .member-description,
.tech-card .member-description {
    font-size: 0.8rem;
    line-height: 1.4;
}

/* 响应式设计 */
@media (max-width: 1200px) {
    .business-card {
        width: calc(33.33% - 1.5rem);
        /* 一行三个 */
    }

    .tech-card {
        width: calc(25% - 1rem);
        /* 一行四个 */
    }
}

@media (max-width: 992px) {
    .teacher-card {
        flex-direction: column;
        max-width: 500px;
    }

    .teacher-avatar {
        width: 100%;
        min-width: 100%;
    }

    .teacher-avatar img {
        aspect-ratio: 3/2;
    }

    .teacher-info {
        text-align: center;
    }

    .business-card {
        width: calc(50% - 1.5rem);
        /* 一行两个 */
    }

    .tech-card {
        width: calc(33.33% - 1rem);
        /* 一行三个 */
    }
}

@media (max-width: 768px) {
    .tech-card {
        width: calc(50% - 1rem);
        /* 一行两个 */
    }
}

@media (max-width: 576px) {

    .business-card,
    .tech-card {
        width: 100%;
        /* 一行一个 */
        max-width: 350px;
    }
}
</style>