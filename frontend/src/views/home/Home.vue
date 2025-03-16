<script setup lang="ts">
import { InfoFilled, Warning, Memo, DataLine, Phone, Message, Location, Service } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const navigateTo = (path: string) => {
  router.push(path)
}
const funcs = [
  { icon: Warning, title: '风险信号', path: '/risk-signal' },
  { icon: Service, title: '智能助手', path: '/chat' },
  { icon: DataLine, title: '外汇数据', path: '/forex-data' },
  { icon: Memo, title: '新闻资讯', path: '/news' }
]
</script>

<template>
  <el-main class="home-container">
    <div class="content-wrapper">
      <!-- 介绍栏 -->
      <div class="intro">
        <div class="intro-header">
          <h1 class="intro-title">Risk Hunter</h1>
          <p class="intro-subtitle">您身边的外汇风险管理助手</p>
        </div>
        <div class="intro-about">
          <el-icon>
            <InfoFilled />
          </el-icon>
          <span>关于我们</span>
        </div>
        <p class="intro-description">
          Risk Hunter 是一个前沿的统计与AI工具，能够整合分析过往趋势，通过多维度的数据模型精准捕捉汇率风险信号，为企业提供更具前瞻性、科学性的风险管理支持。
        </p>
      </div>

      <!-- 功能栏 -->
      <div class="function-grid">
        <el-card v-for="(item, index) in funcs" :key="index" class="function-card" :class="{
          'risk-signal-card': item.title === '风险信号',
          'smart-assistant-card': item.title === '智能助手',
          'forex-data-card': item.title === '外汇数据',
          'news-card': item.title === '新闻资讯'
        }">
          <div class="function-icon" v-if="!['风险信号', '智能助手', '外汇数据', '新闻资讯'].includes(item.title)">
            <el-icon>
              <component :is="item.icon" />
            </el-icon>
          </div>
          <div class="function-content" :class="{
            'risk-signal-content': item.title === '风险信号',
            'smart-assistant-content': item.title === '智能助手',
            'forex-data-content': item.title === '外汇数据',
            'news-content': item.title === '新闻资讯'
          }">
            <h3 class="card-title">{{ item.title }}</h3>
            <!-- 风险信号描述 -->
            <p class="card-description" v-if="item.title === '风险信号'">
              RiskHunter构建数据驱动的智能预测引擎，整合World Bank、Wind等传统金融数据与外汇管理局、新浪财经等另类数据源。基于DeepSeek
              API构建智能ETL管道，通过LSTM时序建模与DCC-GARCH动态相关性分析，建立多维度风险预警指标体系。样本外回测显示预测准确率提升15%-20%，在EUR/USD等八大货币对中保持超85%的风险识别精度，兼具统计显著性与业务可解释性。
            </p>

            <!-- 智能助手描述 -->
            <p class="card-description" v-if="item.title === '智能助手'">
              RiskHunter打造您的专属外汇风险管理智能助手。通过接入DeepSeek和Qwen等大语言模型API，结合RAG检索增强生成技术和海量数字知识库，RiskHunter智能助手成为外汇风险管理领域的专家。RiskHunter支持直接导入用户信息和风险信号数据进行智能问答，为您提供优质的专业知识解释和定制的风险管理建议。
            </p>

            <!-- 外汇数据描述 -->
            <p class="card-description" v-if="item.title === '外汇数据'">
              RiskHunter整合权威外汇数据信息。通过调用专业平台的API，实现数据的实时展示与高频更新，为您的风险管理保驾护航。运用大量清晰易懂的统计图表，将复杂的金融数据可视化，所见即所得。
            </p>

            <!-- 新闻资讯描述 -->
            <p class="card-description" v-if="item.title === '新闻资讯'">
              RiskHunter整理外汇领域重要新闻政策，致力于打破用户的信息差。通过引用国家外汇管理局、新浪财经等官方平台和权威媒体的政策消息和新闻资讯，让您实时掌握市场动态，规避潜在风险。
            </p>

            <el-button type="primary" @click="navigateTo(item.path)" class="card-button">
              查看详情
            </el-button>
          </div>
        </el-card>
      </div>

      <!-- 联系栏 -->
      <div class="contact-container">
        <div class="contact-header">
          <el-icon>
            <Phone />
          </el-icon>
          <h3 class="contact-title">联系我们</h3>
        </div>

        <div class="contact-content">
          <div class="contact-item">
            <el-icon class="contact-icon">
              <Message />
            </el-icon>
            <span>邮箱：</span>
            <a href="mailto:riskhunter@smail.nju.edu.cn">
              riskhunter@smail.nju.edu.cn
            </a>
          </div>

          <div class="contact-item">
            <el-icon class="contact-icon">
              <Location />
            </el-icon>
            <span>地址：中国江苏省南京市栖霞区南京大学</span>
          </div>
        </div>
      </div>
    </div>
  </el-main>
</template>

<style scoped>
/* 主容器 */
.home-container {
  background: linear-gradient(135deg, #e0f7fa 0%, #cfb0d4 100%);
  padding: 0;
  min-height: 100vh;
  display: flex;
  justify-content: center;
  overflow-x: hidden;
  /* 防止水平滚动 */
}

/* 内容包装器 - 限制最大宽度并居中 */
.content-wrapper {
  width: 100%;
  max-width: 100%;
  box-sizing: border-box;
  /* 确保内边距计入宽度 */
}

/* 介绍栏 */
.intro {
  margin-bottom: 2rem;
  text-align: center;
  padding: 2rem 1rem;
  box-sizing: border-box;
}

.intro-header {
  margin-bottom: 1.5rem;
}

/* 修改标题样式 */
.intro-title {
  color: #303030;
  /* 更深的颜色 */
  font-size: clamp(2rem, 5vw, 3.5rem);
  /* 响应式字体大小 */
  font-weight: bold;
  /* 加粗 */
  margin-bottom: 0.5rem;
  line-height: 1.2;
}

.intro-subtitle {
  color: #303030;
  font-size: clamp(1.5rem, 3vw, 2.5rem);
  /* 响应式字体大小 */
  font-weight: bold;
  white-space: nowrap;
  overflow: hidden;
  /* 防止换行 */
  text-align: center;
  /* 确保文字居中 */
  display: inline-block;
  /* 确保宽度适应内容 */
  width: 0;
  /* 初始宽度为0 */
  border-right: .15em solid #666;
  /* 光标 */
  animation: typing 1.5s steps(40, end) forwards, blink-caret .75s step-end infinite;
}

/* 打字机动画 - 使用媒体查询设置不同宽度下的终止宽度 */
@keyframes typing {
  from {
    width: 0%
  }

  to {
    width: 32%
  }

  /* 增加宽度比例确保文本完全显示 */
}

/* 在较窄屏幕上设置更大的宽度比例 */
@media (max-width: 1600px) {
  @keyframes typing {
    from {
      width: 0%
    }

    to {
      width: 40%
    }
  }
}

@media (max-width: 768px) {
  @keyframes typing {
    from {
      width: 0%
    }

    to {
      width: 60%
    }
  }

  .intro-subtitle {
    max-width: 100%;
    /* 确保在小屏上不超出容器 */
  }
}

/* 光标闪烁动画 */
@keyframes blink-caret {

  from,
  to {
    border-color: transparent
  }

  50% {
    border-color: #2649f8;
  }
}

/* 关于我们样式优化 */
.intro-about {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
  /* 增加间距 */
  margin: 1.8rem 0;
  /* 增加上下边距 */
  color: #512da8;
  /* 更深的蓝紫色 */
  font-size: 1.5rem;
  /* 增大字体 */
  font-weight: 500;
  /* 稍微加粗 */
}

.intro-description {
  color: #303030;
  /* 改为指定颜色 */
  line-height: 1.8;
  max-width: 800px;
  margin: 0 auto;
  text-align: justify;
  font-size: 1.1rem;
  /* 增大字体 */
  padding: 0 1rem;
  /* 添加左右内边距 */
}

/* 功能卡片标题样式 */
.card-title {
  margin-bottom: 1.8rem;
  font-size: 4rem;
  /* 增大字体 */
  color: #303030;
  /* 更改颜色 */
  text-align: center;
  font-weight: 600;
  /* 加粗 */
  position: relative;
  padding-bottom: 0.8rem;
}

/* 为标题添加下划线装饰 */
.card-title::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);
  width: 60px;
  height: 3px;
  background: #626aef;
  border-radius: 2px;
}

/* 查看详情按钮 - 调整宽度和内边距 */
.card-button {
  width: 300px;
  /* 增加按钮宽度 */
  height: 50px;
  /* 保持高度不变 */
  border-radius: 32px;
  /* 保持圆角比例 */
  background-color: #626aef;
  text-align: center;
  color: white;
  font-size: 1.2rem;
  /* 设置合适的字体大小 */
  font-weight: 500;
  transition: all 0.3s;
  margin-top: 0px;
  /* 增加上边距 */
  display: flex;
  /* 使用flex布局使文本居中 */
  align-items: center;
  justify-content: center;
  margin-left: auto;
  /* 使用auto实现水平居中 */
  margin-right: auto;
  padding: 0 60px;
  /* 增加左右内边距 */
}

.card-button:hover {
  background-color: #535bd4;
  transform: translateY(-3px);
  box-shadow: 0 6px 16px rgba(98, 106, 239, 0.4);
}

/* 卡片描述文本样式 */
.card-description {
  color: #303030;
  font-size: 1rem;
  line-height: 2;
  text-align: justify;
  margin-bottom: 1.5rem;
  max-width: 90%;
  margin-left: auto;
  margin-right: auto;
}

/* 功能栏 */
.function-grid {
  display: flex;
  flex-direction: column;
  width: 100%;
  box-sizing: border-box;
}

.function-card {
  margin-bottom: 1rem;
  padding: 1.5rem;
  border: 1px solid var(--el-border-color-light);
  transition: all 0.3s var(--el-transition-function-fast-bezier);
  background: linear-gradient(to right, #dbeafe, #f3e5f5);
  display: flex;
  flex-direction: column;
  justify-content: center;
  height: 600px;
  box-sizing: border-box;
}

.risk-signal-card {
  background: linear-gradient(to right, rgba(255, 255, 255, 0), rgba(255, 255, 255, 0.5)), url('../../assets/网站设计素材/风险信号背景.png') no-repeat left center / contain;
  display: flex;
  align-items: center;
}

.risk-signal-content .card-title {
  font-size: 2.5rem;
}

.risk-signal-content {
  margin-left: auto;
  padding-left: 50%;
}
/* 智能助手卡片样式 */
.smart-assistant-card {
  background: #ffffff url('../../assets/网站设计素材/智能助手背景.png') no-repeat right center / contain;
  display: flex;
  align-items: center;
}

.smart-assistant-content {
  margin-right: auto;
  padding-right: 50%;
  padding-left: 5%;
}

/* 外汇数据卡片样式 */
.forex-data-card {
  background: rgba(235, 237, 250, 1) url('../../assets/网站设计素材/外汇数据背景.png') no-repeat left center / contain;
  position: relative;
  display: flex;
  align-items: center;
}

.forex-data-card::after {
  content: "";
  position: absolute;
  top: 0;
  left: 65%;
  width: 50%;
  height: 100%;
  background: linear-gradient(to right, rgba(235, 237, 250, 0.6), rgba(235, 237, 250, 1));
  pointer-events: none;
}

.forex-data-content {
  margin-left: auto;
  padding-left: 50%;
  position: relative;
  z-index: 2;
}

/* 新闻资讯卡片样式 */
.news-card {
  background: rgb(235, 243, 249) url('../../assets/网站设计素材/新闻资讯背景.png') no-repeat right center / contain;
  position: relative;
  display: flex;
  align-items: center;
}

.news-card::after {
  content: "";
  position: absolute;
  top: 0;
  right: 60%;
  width: 50%;
  height: 100%;
  background: linear-gradient(to left, rgba(235, 243, 249, 0.8), rgba(235, 243, 249, 0.5));
  pointer-events: none;
}

.news-content {
  margin-right: auto;
  padding-right: 50%;
  padding-left: 5%;
  position: relative;
  z-index: 2;
}

.smart-assistant-content .card-title {
  font-size: 2.5rem;
}

.forex-data-content .card-title {
  font-size: 2.5rem;
}

.news-content .card-title {
  font-size: 2.5rem;
}


/* 响应式调整 - 中等屏幕 */
@media (max-width: 1200px) {
  .risk-signal-content .card-title {
    font-size: 2rem;
    /* 缩小标题字体 */
  }

  .risk-signal-content .card-description {
    font-size: 0.95rem;
    /* 稍微缩小描述文字 */
    line-height: 1.8;
    /* 减少行间距 */
    margin-bottom: 1.2rem;
  }

  .risk-signal-content .card-button {
    width: 250px;
    /* 按比例缩小按钮宽度 */
    height: 45px;
    /* 稍微减小高度 */
    font-size: 1.1rem;
    /* 略微减小字体 */
    padding: 0 50px;
    /* 调整内边距 */
  }
}

/* 响应式调整 - 小屏幕 */
@media (max-width: 768px) {
  .risk-signal-content .card-title {
    font-size: 1.8rem;
    /* 更小的标题字体 */
    margin-bottom: 1.2rem;
    /* 减少底部边距 */
    padding-bottom: 0.6rem;
    /* 减少下划线边距 */
  }

  .risk-signal-content .card-description {
    font-size: 0.9rem;
    /* 进一步缩小描述文字 */
    line-height: 1.6;
    /* 更紧凑的行间距 */
    margin-bottom: 1rem;
    max-width: 95%;
    /* 增加宽度比例 */
  }

  .risk-signal-content .card-button {
    width: 220px;
    /* 进一步缩小按钮宽度 */
    height: 40px;
    /* 减小高度 */
    font-size: 1rem;
    /* 减小字体 */
    padding: 0 40px;
    /* 减少内边距 */
  }
}

/* 响应式调整 - 超小屏幕 */
@media (max-width: 480px) {
  .risk-signal-content .card-title {
    font-size: 1.5rem;
    /* 最小标题字体 */
  }

  .risk-signal-content .card-description {
    font-size: 0.85rem;
    /* 最小描述文字 */
    line-height: 1.5;
    /* 最小行间距 */
  }

  .risk-signal-content .card-button {
    width: 180px;
    /* 最小按钮宽度 */
    height: 38px;
    /* 最小高度 */
    padding: 0 30px;
    /* 最小内边距 */
  }
}

/* 响应式设计 - 智能助手卡片 */
@media (max-width: 1200px) {
  .smart-assistant-content .card-title {
    font-size: 2rem;
  }
  
  .smart-assistant-content .card-description {
    font-size: 0.95rem;
    line-height: 1.8;
    margin-bottom: 1.2rem;
  }
  
  .smart-assistant-content .card-button {
    width: 250px;
    height: 45px;
    font-size: 1.1rem;
    padding: 0 50px;
  }
}

@media (max-width: 768px) {
  .smart-assistant-content .card-title {
    font-size: 1.8rem;
    margin-bottom: 1.2rem;
    padding-bottom: 0.6rem;
  }
  
  .smart-assistant-content .card-description {
    font-size: 0.9rem;
    line-height: 1.6;
    margin-bottom: 1rem;
    max-width: 95%;
  }
  
  .smart-assistant-content .card-button {
    width: 220px;
    height: 40px;
    font-size: 1rem;
    padding: 0 40px;
  }
}

@media (max-width: 480px) {
  .smart-assistant-content .card-title {
    font-size: 1.5rem;
  }
  
  .smart-assistant-content .card-description {
    font-size: 0.85rem;
    line-height: 1.5;
  }
  
  .smart-assistant-content .card-button {
    width: 180px;
    height: 38px;
    padding: 0 30px;
  }
}

/* 响应式设计 - 外汇数据卡片 */
@media (max-width: 1200px) {
  .forex-data-content .card-title {
    font-size: 2rem;
  }
  
  .forex-data-content .card-description {
    font-size: 0.95rem;
    line-height: 1.8;
    margin-bottom: 1.2rem;
  }
  
  .forex-data-content .card-button {
    width: 250px;
    height: 45px;
    font-size: 1.1rem;
    padding: 0 50px;
  }
}

@media (max-width: 768px) {
  .forex-data-content .card-title {
    font-size: 1.8rem;
    margin-bottom: 1.2rem;
    padding-bottom: 0.6rem;
  }
  
  .forex-data-content .card-description {
    font-size: 0.9rem;
    line-height: 1.6;
    margin-bottom: 1rem;
    max-width: 95%;
  }
  
  .forex-data-content .card-button {
    width: 220px;
    height: 40px;
    font-size: 1rem;
    padding: 0 40px;
  }
}

@media (max-width: 480px) {
  .forex-data-content .card-title {
    font-size: 1.5rem;
  }
  
  .forex-data-content .card-description {
    font-size: 0.85rem;
    line-height: 1.5;
  }
  
  .forex-data-content .card-button {
    width: 180px;
    height: 38px;
    padding: 0 30px;
  }
}

/* 响应式设计 - 新闻资讯卡片 */
@media (max-width: 1200px) {
  .news-content .card-title {
    font-size: 2rem;
  }
  
  .news-content .card-description {
    font-size: 0.95rem;
    line-height: 1.8;
    margin-bottom: 1.2rem;
  }
  
  .news-content .card-button {
    width: 250px;
    height: 45px;
    font-size: 1.1rem;
    padding: 0 50px;
  }
}

@media (max-width: 768px) {
  .news-content .card-title {
    font-size: 1.8rem;
    margin-bottom: 1.2rem;
    padding-bottom: 0.6rem;
  }
  
  .news-content .card-description {
    font-size: 0.9rem;
    line-height: 1.6;
    margin-bottom: 1rem;
    max-width: 95%;
  }
  
  .news-content .card-button {
    width: 220px;
    height: 40px;
    font-size: 1rem;
    padding: 0 40px;
  }
}

@media (max-width: 480px) {
  .news-content .card-title {
    font-size: 1.5rem;
  }
  
  .news-content .card-description {
    font-size: 0.85rem;
    line-height: 1.5;
  }
  
  .news-content .card-button {
    width: 180px;
    height: 38px;
    padding: 0 30px;
  }
}

/* 设置el-card组件的样式 */
:deep(.el-card) {
  width: 100%;
  margin: 0;
  border-radius: 0;
  overflow: hidden;
}

:deep(.el-card__body) {
  width: 100%;
  padding: 0;
  box-sizing: border-box;
}

.function-card:hover {
  transform: translateY(-5px);
  box-shadow: var(--el-box-shadow-light);
}

.function-icon {
  display: flex;
  justify-content: center;
  font-size: 2.5rem;
  margin-bottom: 1.2rem;
  color: var(--el-color-primary);
}

.card-title {
  margin-bottom: 1.5rem;
  font-size: 1.5rem;
  color: var(--el-text-color-primary);
  text-align: center;
  font-weight: 500;
}

.card-button {
  width: fit-content;
  margin: auto;
  display: block;
}

/* 联系栏 */
.contact-container {
  padding: 2rem 1rem;
  box-sizing: border-box;
  width: 100%;
  margin-bottom: 2rem;
}

.contact-header {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px; /* 与关于我们保持一致的间距 */
  margin: 1.8rem 0; /* 与关于我们一致的边距 */
}

.contact-title {
  color: #512da8; /* 使用相同的蓝紫色 */
  margin: 0;
  font-size: 1.5rem; /* 与关于我们一致的字体大小 */
  font-weight: 500; /* 与关于我们一致的字体粗细 */
}

.contact-header .el-icon {
  color: #512da8; /* 图标也使用相同色调 */
  font-size: 1.5rem; /* 与文字大小保持一致 */
}

.contact-content {
  max-width: 800px;
  margin: 0 auto;
  color: #303030; /* 与关于我们内容一致的颜色 */
  line-height: 1.8;
}

.contact-item {
  display: flex;
  align-items: center;
  gap: 12px; /* 与关于我们保持一致的间距 */
  margin-bottom: 1rem;
  justify-content: center;
  font-size: 1.1rem; /* 与介绍描述一致的字体大小 */
}

.contact-icon {
  font-size: 1.2rem;
  color: #512da8; /* 图标颜色与标题一致 */
}

/* 链接样式 */
.contact-item a {
  color: #626aef; /* 使用按钮颜色作为链接颜色 */
  text-decoration: none;
  transition: color 0.3s;
}

.contact-item a:hover {
  color: #535bd4; /* 悬停颜色与按钮悬停颜色一致 */
  text-decoration: underline;
}
</style>