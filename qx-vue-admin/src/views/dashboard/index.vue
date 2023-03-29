<template>
  <div class="dashboard-container">
    <div class="dashboard-text">
      个人博客管理系统
    </div>
    <div>
      <scpn class="sc">项目技术:</scpn>
    </div><br/>
    <scpn class="sq">前端:采用vue实现</scpn><br/><br/>
    <scpn class="sq">后端:SpringBoot做基础框架、SpringSecurity做授权与认证，
      并使用Redis做缓存,Mybatis-plus做数据访问层控制,数据库使用MySQL。</scpn>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
export default {
  name: 'Dashboard',
  computed: {
    ...mapGetters([
      'name'
    ])
  }
}
</script>

<style lang="scss" scoped>
.dashboard {
  &-container {
    margin: 30px;
  }
  &-text {
    font-size: 30px;
    line-height: 46px;
  }
}
.sc{
  margin: 50px;
}
.sq{
  margin: 70px;
}
</style>
