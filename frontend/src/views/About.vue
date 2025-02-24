<script lang="ts">
import { defineComponent } from 'vue'

export default defineComponent({
  name: 'About'
})
</script>

<template>
  <div class="about">
    <h1>Risk Hunter</h1>
    <div class="content">
      <section class="intro">
        <h2>关于我们</h2>
        <p>Risk Hunter 是一个前沿的统计与AI工具，能够整合分析过往趋势，通过多维度的数据模型精准捕捉汇率风险信号，为企业和金融机构提供更具前瞻性、科学性的风险管理支持。</p>
      </section>

      <section class="features">
        <h2>核心功能</h2>
        <ul>
          <li>分析不同因素对汇率的影响，形成单一货币对的风险信号，展示回测效果。</li>
          <li>针对多货币敞口的情况，根据各货币对的相关性，形成多货币对的风险信号智能风险分析。</li>
          <li>根据对最大回撤比例、交易期限的定制化需求微调信号参数。风险预警系统。</li>
        </ul>
      </section>

      <section class="contact">
        <h2>联系我们</h2>
        <p>邮箱：riskhunter@smail.nju.edu.cn</p>
        <p>地址：中国江苏省南京市栖霞区南京大学</p>
      </section>
    </div>
  </div>
</template>

<style scoped>
.about {
  max-width: 800px;
  margin: 0 auto;
  padding: 2rem;
}

h1 {
  color: #2c3e50;
  text-align: center;
  margin-bottom: 2rem;
}

.content {
  background: #ffffff;
  border-radius: 8px;
  padding: 2rem;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

section {
  margin-bottom: 2rem;
}

h2 {
  color: #42b883;
  margin-bottom: 1rem;
  border-bottom: 2px solid #42b883;
  padding-bottom: 0.5rem;
}

ul {
  list-style: none;
  padding-left: 0;
}

li {
  padding: 0.5rem 0;
  position: relative;
  padding-left: 1.5rem;
}

li::before {
  content: "•";
  color: #42b883;
  font-size: 1.5rem;
  position: absolute;
  left: 0;
}

.contact p {
  margin: 0.5rem 0;
  color: #666;
}
</style>