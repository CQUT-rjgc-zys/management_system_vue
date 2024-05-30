<!-- 外勤人员系统 -->
<template>
  <div class='workerhome'>
    <el-container>
      <el-aside width="200px" class="aside">
        <h2>企业外勤管理系统</h2>
        <el-menu router active-text-color="#78e8ec" background-color="#005579" default-active="1" class="aside-menu"
          text-color="#fff">

          <el-menu-item index="/workOrder"><el-icon><Histogram /></el-icon>个人工单管理</el-menu-item>
          <el-menu-item index="/myTaskList"><el-icon><Collection /></el-icon>个人外勤任务</el-menu-item>
          <el-menu-item index="/message"><el-icon><Message /></el-icon>消息提示</el-menu-item>
          <el-menu-item index="/personalInfo"><el-icon><UserFilled /></el-icon>个人中心</el-menu-item>
          <!-- <el-menu-item index="/location">定位</el-menu-item> -->

        </el-menu>
      </el-aside>

      <el-container>

        <el-header class="header">
          <el-menu class="header-menu" mode="horizontal" background-color="#005579" text-color="#fff"
            active-text-color="#78e8ec">
            <el-menu-item index="1">外勤人员系统</el-menu-item>
            <el-sub-menu index="2">

              <template #title>
                <el-icon>
                  <UserFilled />
                </el-icon>
                User
              </template>
              <el-menu-item index="/personalInfo">个人中心</el-menu-item>
              <el-menu-item index="/">退出登录</el-menu-item>
            </el-sub-menu>
          </el-menu>
        </el-header>

        <el-main>
          <el-scrollbar max-height="600px">

            <div class="content">
              <router-view></router-view>
            </div>

          </el-scrollbar>
        </el-main>

      </el-container>
    </el-container>

  </div>
</template>

<script setup lang='ts'>
  import { ref, reactive } from 'vue'
  import { Edit, UserFilled, LocationFilled, List, Message,Histogram,Collection } from '@element-plus/icons-vue'
  import TpScroll from '@/utils/tp-scroll.js'
  import ScrollContainer from '@c/scrollContainer'

  TpScroll.RemoveScroll();



</script>

<style scoped lang='scss'>
  .workerhome {
    width: 100vw;
    height: 100vh;
    /* background: url("@/assets/inbg.jpg"); */

    .aside {
      width: 200px;
      height: 100vh;
      background-color: #005579;
      color: #fff;

      h2 {
        font-size: 20px;
        font-family: 'Courier New', Courier, monospace;
        line-height: 40px;
        height: 40px;
      }

      .aside-menu {
        border: none;

      }
    }

    .header {
      height: 60px;
      background-color: #005579;
      color: #fff;

      .header-menu {
        border: none;
        display: flex;
        justify-content: flex-end;

      }
    }

    .content {
      width: 100%;
      height: auto;
      background-color: #fff;
    }

  }
</style>