<script setup lang="ts">
import ExchangeRateBoard from '../../components/signal/ExchangeRateBoard.vue';
import RiskScore from '../../components/signal/RiskScore.vue';
import AlertList from '../../components/signal/AlertList.vue';
import ExposureMatrix from '../../components/signal/ExposureMatrix.vue';
import PredictionAndAdvice from '../../components/signal/PredictionAndAdvice.vue';
//import RiskAMap from '../../components/signal/RiskAMap.vue'; // 导入风险地图组件
//import RiskGMap from '../../components/signal/RiskGMap.vue'; // 导入风险地图组件
import RiskMap from '../../components/signal/RiskMap.vue'; // 导入风险地图组件

</script>

<template>
  <el-main class="signal-container">
    <el-row :gutter="20" class="signal-row">
      <el-col :xs="24" :sm="24" :md="8" :lg="8" class="signal-col">
        <ExchangeRateBoard />
      </el-col>

      <el-col :xs="24" :sm="24" :md="8" :lg="8" class="signal-col">
        <RiskScore />
      </el-col>

      <el-col :xs="24" :sm="24" :md="8" :lg="8" class="signal-col">
        <AlertList />
      </el-col>
    </el-row>

    <el-row :gutter="20" class="signal-row">
      <el-col :span="24" class="signal-col">
        <RiskMap height="700px" /> <!-- 使用风险地图组件 -->
      </el-col>
    </el-row>


    <el-row :gutter="20" class="signal-row">
      <el-col :span="24" class="signal-col">
        <ExposureMatrix />
      </el-col>
    </el-row>

    <el-row :gutter="20" class="signal-row">
      <el-col :span="24" class="signal-col">
        <PredictionAndAdvice />
      </el-col>
    </el-row>
  </el-main>
</template>

<style scoped>
.signal-container {
  padding: 20px;
  background-color: var(--el-bg-color-page);
}

.signal-row {
  width: 90%;
  margin-bottom: 20px;
}

.signal-col {
  margin-bottom: 20px;
}

@media (max-width: 768px) {
  .signal-container {
    padding: 10px;
  }

  .signal-row {
    margin-bottom: 10px;
  }
}
</style>