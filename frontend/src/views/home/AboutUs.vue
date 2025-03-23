<script setup lang="ts">
import { ref } from 'vue';
import { InfoFilled } from '@element-plus/icons-vue';
import MarkdownIt from 'markdown-it';

// 定义选项卡选择状态
const activeTab = ref('teachers');

// 导入Markdown-it，增加链接自动识别
const md = new MarkdownIt({
  linkify: true,
  typographer: true
});

// 使用URL构造器处理所有图片路径
const teacherImages = {
  jiangY: new URL('../../assets/teamMembers/JiangY.jpg', import.meta.url).href,
  maoYL: new URL('../../assets/teamMembers/MaoYL.jpg', import.meta.url).href
};

const techImages = {
  cz: new URL('../../assets/teamMembers/IT1_cz.jpg', import.meta.url).href,
  oly: new URL('../../assets/teamMembers/IT2_oly.jpg', import.meta.url).href,
  zyy: new URL('../../assets/teamMembers/IT3_zyy.jpg', import.meta.url).href,
  crz: new URL('../../assets/teamMembers/IT4_crz.jpg', import.meta.url).href,
  lyy: new URL('../../assets/teamMembers/IT5_lyy.jpg', import.meta.url).href
};

// 默认头像
const defaultAvatar = new URL('../assets/teamMembers/arknights.jpg', import.meta.url).href;

// 定义各组成员数据，使用Markdown格式
const teachers = [
    {
        name: '蒋彧',
        title: '南京大学商学院',
        description: `**南京大学商学院金融与保险学系教授、博士生导师**，江苏高校"青蓝工程"中青年学术带头人。

**教育背景**
- 2001年 南京大学理学学士
- 2004年 美国科罗拉多矿业学院（Colorado School of Mines）理学硕士
- 2007年 美国爱荷华大学（University of Iowa）精算学硕士
- 2009年 美国爱荷华大学（University of Iowa）理学博士

**研究领域**：金融市场、行为金融、金融计量等

**学术成果**：
- SSCI/SCI来源期刊（如Journal of Econometrics）论文20余篇
- CSSCI来源期刊（《金融研究》、《管理科学学报》等）论文近30篇

**科研项目**：
- 国家自然科学基金 1项
- 教育部人文社科基金 1项
- 教育部博士点基金 1项
- 国家博士后基金 2项

**学术兼职**：
- 《南大商学评论》执行编辑
- 多家国内外期刊匿名审稿人`,
        avatar: teacherImages.jiangY
    },
    {
        name: '毛云龙',
        title: '南京大学软件学院',
        description: `**南京大学准聘副教授**，南京大学计算机科学与技术系获学士学位和博士学位。导师为**仲盛教授**。

**研究方向**：
- 网络安全
- 数据隐私保护
- 机器学习安全
- 区块链

**学术成果**：研究成果发表在Security、CCS、ESORICS、IEEE TIFS、IEEE TDSC等国际会议和期刊。

**学术兼职**：多次担任**数据安全和机器学习**等领域国际会议的程序委员。

**科研项目**：
- 国家自然科学基金面上项目
- 国家自然科学基金青年项目
- 江苏省科技厅优秀青年基金项目
- 骨干成员参与国家自然科学基金杰出青年科学基金项目
- 国家重点研发计划专项课题

**产业合作**：长期保持和**华为、字节跳动、美团、百度等**企业的研究合作，工作成果在企业实践中得到了应用和验证。`,
        avatar: teacherImages.maoYL
    },
];

const businessTeam = [
    {
        name: '陈展',
        title: '后端开发与大语言模型处理',
        description: '南京大学软件学院23级本科生，曾获2024年美国大学生数学建模竞赛H奖、2023-2024年度人民奖学金。负责RiskHunter项目的后端开发与测试工作、大模型的训练与优化工作，为智能助手引入了流式输出和RAG检索增强生成等优化技术。',
        avatar: techImages.cz
    },
    {
        name: '陈明',
        title: '数据分析师',
        description: '南京大学商学院，专注于外汇市场分析与预测',
        avatar: defaultAvatar
    },
    {
        name: '赵婷',
        title: '商业顾问',
        description: '南京大学商学院市场营销专业，负责市场需求分析',
        avatar: defaultAvatar
    },
    {
        name: '周楠',
        title: '金融顾问',
        description: '南京大学商学院金融学，专注金融风险管理研究',
        avatar: defaultAvatar
    },
];

const techTeam = [
    {
        name: '陈展',
        title: '后端开发与大语言模型处理',
        description: '南京大学软件学院2023级本科生，曾获2024年美国大学生数学建模竞赛H奖、2023-2024年度人民奖学金等。负责RiskHunter项目的后端开发与测试工作、大模型的训练与优化工作，为智能助手建立了系统提示词工程，引入了流式输出和RAG检索增强生成等模型优化技术。',
        avatar: techImages.cz
    },
    {
        name: '欧立言',
        title: '前端开发与人机交互设计',
        description: '南京大学软件学院2023级本科生，曾获2023-2024年度人民奖学金等。负责RiskHunter项目的前端开发与用户界面设计工作，为智能助手引入了多轮对话等技术，负责撰写用户手册等技术文档，负责各类数据来源API的接口调通，为风险信号看板等功能提供了友好的用户交互体验。',
        avatar: techImages.oly
    },
    {
        name: '张耀宇',
        title: '技术负责人、数据处理与前后端交互',
        description: '南京大学软件学院2023级本科生，曾获2023-2024年度国家奖学金、行知奖学金、2024年全国大学生数学竞赛江苏省三等奖、百度"星河杯"三等奖、最佳人气奖等。负责RiskHunter项目的技术架构设计、数据处理与前后端交互工作，对另类数据进行爬取并利用LLM的API进行处理。',    
        avatar: techImages.zyy
    },
    {
        name: '陈瑞泽',
        title: '机器学习模型搭建与优化',
        description: '南京大学软件学院2023级本科生，曾获2023-2024年度人民奖学金一等奖、EL程序设计竞赛三等奖等，曾参与有关机器学习的科研训练项目。负责外汇风险预测模型的构建、分析和训练，搭建了主要的机器学习模型和神经网络模型，为外汇风险信号的计算和建模提供了主要支持。',
        avatar: techImages.crz
    },
    {
        name: '龙彦宇',
        title: '神经网络模型训练与分析',
        description: '南京大学人工智能学院2023级本科生，曾获2023-2024年度人民奖学金一等奖、2024年全国大学生数学竞赛江苏省一等奖、百度"星河杯"三等奖、最佳人气奖等。负责外汇风险预测模型的训练、分析和优化，对外汇风险信号的指标和预测模型进行了关键优化。',
        avatar: techImages.lyy
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

            <!-- 指导教师部分 - 统一布局 -->
            <div class="team-members" v-if="activeTab === 'teachers'">
                <div class="teachers-row">
                    <!-- 教师卡片 - 统一布局，渲染Markdown内容 -->
                    <div class="teacher-card" v-for="(teacher, index) in teachers" :key="index">
                        <div class="teacher-left">
                            <div class="teacher-avatar">
                                <img :src="teacher.avatar" alt="教师头像">
                            </div>
                            <div class="teacher-brief">
                                <h3 class="member-name">{{ teacher.name }}</h3>
                                <div class="member-title">{{ teacher.title }}</div>
                            </div>
                        </div>
                        <div class="teacher-info">
                            <!-- 使用v-html和md.render渲染Markdown -->
                            <div class="markdown-content" v-html="md.render(teacher.description)"></div>
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
    max-width: 1600px;
    margin: 0 auto;
    padding: 2rem 1rem;
    text-align: center;
}

/* 标题容器 */
.about-header {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 12px;
    margin: 1.8rem 0;
}

/* 标题样式 */
.about-title {
    color: #6a56c6;
    font-size: 1.5rem;
    font-weight: 500;
    margin: 0;
    position: relative;
}

/* 移除原来的下划线效果 */
.about-title::after {
    content: none;
}

/* 图标样式 */
.about-header .el-icon {
    color: #6a56c6;
    font-size: 1.5rem;
}

/* 描述文本 */
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

/* 选项卡容器 */
.tab-selector {
    display: flex;
    justify-content: center;
    margin-bottom: 2rem;
    border-bottom: 1px solid #e0e0e0;
}

/* 选项卡项目 */
.tab-item {
    padding: 1rem 2rem;
    cursor: pointer;
    color: #303030;
    font-size: 1.1rem;
    position: relative;
    transition: all 0.3s;
}

.tab-item:hover {
    color: #6a56c6;
}

.tab-item.active {
    color: #6a56c6;
    font-weight: 500;
}

.tab-item.active::after {
    content: '';
    position: absolute;
    bottom: -1px;
    left: 0;
    width: 100%;
    height: 3px;
    background-color: #6a56c6;
}

/* 团队成员容器 */
.team-members {
    margin-top: 2rem;
}

/* 通用成员卡片样式 */
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
    background-color: #ebedfA;
    box-shadow: 0 10px 20px rgba(0, 0, 0, 0.08), 0 2px 6px rgba(0, 0, 0, 0.05);
    border-radius: 12px;
    border: 1px solid rgba(255, 255, 255, 0.5);
    overflow: hidden;
    transition: transform 0.3s, box-shadow 0.3s;
}

.member-card:hover {
    transform: translateY(-8px);
    box-shadow: 0 15px 30px rgba(0, 0, 0, 0.12), 0 4px 8px rgba(0, 0, 0, 0.08);
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
    background: linear-gradient(to bottom, #ebedfA, #d7daef);
}

.member-name {
    color: #212121;
    margin: 0 0 0.5rem;
    font-size: 1.3rem;
}

.member-title {
    color: #424242;
    font-size: 0.9rem;
    font-weight: 500;
    margin-bottom: 1rem;
}

.member-description {
    color: #424242;
    font-size: 0.9rem;
    line-height: 1.6;
}

/* 指导教师布局样式 */
.teachers-row {
    display: flex;
    flex-direction: row;
    flex-wrap: wrap;
    gap: 2rem;
    margin-bottom: 2rem;
    width: 100%;
    justify-content: center;
}

/* 教师卡片样式 */
.teacher-card {
    width: calc(50% - 2rem);
    max-width: 650px;
    margin: 0;
    background-color: #ebedfA;
    box-shadow: 0 10px 20px rgba(0, 0, 0, 0.08), 0 2px 6px rgba(0, 0, 0, 0.05);
    border-radius: 12px;
    border: 1px solid rgba(255, 255, 255, 0.5);
    overflow: hidden;
    transition: transform 0.3s, box-shadow 0.3s;
    display: flex;
    height: auto;
    padding: 1.8rem;
}

.teacher-card:hover {
    transform: translateY(-8px);
    box-shadow: 0 15px 30px rgba(0, 0, 0, 0.12), 0 4px 8px rgba(0, 0, 0, 0.08);
}

/* 左侧区域 - 包含头像和简介 - 减小宽度 */
.teacher-left {
    width: 140px; /* 减小宽度 */
    display: flex;
    flex-direction: column;
    margin-right: 1.5rem; /* 减小右边距 */
    flex-shrink: 0;
}

/* 头像样式 - 减小尺寸 */
.teacher-avatar {
    width: 120px; /* 减小宽度 */
    height: 160px; /* 减小高度但保持比例 */
    overflow: hidden;
    border-radius: 5px;
    margin-bottom: 1rem;
}

.teacher-avatar img {
    width: 100%;
    height: 100%;
    object-fit: cover;
}

/* 姓名和职位区域 */
.teacher-brief {
    text-align: center;
}

.teacher-brief .member-name {
    margin: 0 0 0.3rem;
    font-size: 1.2rem; /* 稍微减小字体 */
    color: #212121;
}

.teacher-brief .member-title {
    color: #424242;
    font-size: 0.9rem;
    font-weight: 500;
}

/* 右侧介绍信息 - 移除滚动条和高度限制 */
.teacher-info {
    flex: 1;
    display: flex;
    align-items: flex-start;
    padding-left: 1.5rem;
    border-left: 1px solid rgba(0, 0, 0, 0.1);
}

/* Markdown内容样式 */
.markdown-content {
    font-size: 0.9rem; /* 减小字体大小 */
    line-height: 1.6;
    color: #303030;
    text-align: left;
    width: 100%;
}

/* Markdown格式化内容的样式 */
.markdown-content :deep(p) {
    margin: 0.6em 0; /* 减小段落间距 */
}

.markdown-content :deep(strong) {
    color: #6a56c6;
    font-weight: 600;
}

.markdown-content :deep(ul) {
    padding-left: 1.2em;
    margin: 0.6em 0; /* 减小列表间距 */
}

.markdown-content :deep(li) {
    margin-bottom: 0.3em; /* 减小列表项间距 */
}

.markdown-content :deep(h3) {
    margin: 0.8em 0 0.4em; /* 减小标题间距 */
    color: #6a56c6;
    font-size: 1.1rem;
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
    margin-bottom: 1.5rem;
    background-color: #ebedfA;
    box-shadow: 0 10px 20px rgba(0, 0, 0, 0.08), 0 2px 6px rgba(0, 0, 0, 0.05);
    border-radius: 12px;
    border: 1px solid rgba(255, 255, 255, 0.5);
    overflow: hidden;
    transition: transform 0.3s, box-shadow 0.3s;
}

.business-card:hover {
    transform: translateY(-8px);
    box-shadow: 0 15px 30px rgba(0, 0, 0, 0.12), 0 4px 8px rgba(0, 0, 0, 0.08);
}

/* 技术组布局样式 */
.tech-row {
    display: flex;
    flex-wrap: wrap;
    justify-content: center;
    gap: 1rem;
}

.tech-card {
    width: calc(20% - 1.8rem);
    margin-bottom: 1.5rem;
    background-color: #ebedfA;
    box-shadow: 0 10px 20px rgba(0, 0, 0, 0.08), 0 2px 6px rgba(0, 0, 0, 0.05);
    border-radius: 12px;
    border: 1px solid rgba(255, 255, 255, 0.5);
    overflow: hidden;
    transition: transform 0.3s, box-shadow 0.3s;
}

.tech-card:hover {
    transform: translateY(-8px);
    box-shadow: 0 15px 30px rgba(0, 0, 0, 0.12), 0 4px 8px rgba(0, 0, 0, 0.08);
}

/* 商业组和技术组共用的头像样式 - 改为方形但保持尺寸 */
.business-card .member-avatar,
.tech-card .member-avatar {
    width: 150px;
    height: 210px;
    border-radius: 8px; /* 从50%改为8px，创建轻微圆角的方形 */
    overflow: hidden;
    margin: 1.5rem auto 1rem;
    border: 3px solid #fff;
    box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
}

.business-card .member-avatar img,
.tech-card .member-avatar img {
    width: 100%;
    height: 100%;
    object-fit: cover;
}

/* 商业组和技术组的信息部分样式 - 增加比例 */
.business-card .member-info,
.tech-card .member-info {
    padding: 1rem 1.2rem 1.5rem;
    background: linear-gradient(to bottom, #ebedfA, #d7daef);
    flex: 1;
}

.business-card .member-name,
.tech-card .member-name {
    font-size: 1.1rem;
    margin: 0 0 0.4rem;
    color: #212121;
}

.business-card .member-title,
.tech-card .member-title {
    font-size: 0.85rem;
    margin-bottom: 0.8rem;
    color: #424242;
    font-weight: 500;
}

.business-card .member-description,
.tech-card .member-description {
    font-size: 0.85rem;
    line-height: 1.5;
    color: #424242;
    /* 增加描述文字高度 */
    min-height: 100px;
}

/* 响应式设计 */
@media (max-width: 1200px) {
    .teacher-card {
        width: calc(50% - 2rem);
    }
    
    .business-card {
        width: calc(33.33% - 1.5rem);
    }
    
    .tech-card {
        width: calc(25% - 1rem);
    }
}

@media (max-width: 992px) {
    .teachers-row {
        flex-direction: column;
        align-items: center;
    }
    
    .teacher-card {
        width: 100%;
        max-width: 500px;
        flex-direction: column;
    }
    
    .teacher-left {
        width: 100%;
        align-items: center;
        margin-right: 0;
        margin-bottom: 1.5rem;
    }
    
    .teacher-avatar {
        width: 120px;
        height: 160px;
    }
    
    .teacher-info {
        width: 100%;
        padding-left: 0;
        border-left: none;
        padding-top: 1.5rem;
        border-top: 1px solid rgba(0, 0, 0, 0.1);
        text-align: center;
    }
    
    .markdown-content {
        text-align: left;
    }
    
    .business-card {
        width: calc(50% - 1.5rem);
    }
    
    .tech-card {
        width: calc(33.33% - 1rem);
    }
}

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
    
    .tech-card {
        width: calc(50% - 1rem);
    }
}

@media (max-width: 576px) {
    .business-card,
    .tech-card {
        width: 100%;
        max-width: 350px;
    }
}
</style>