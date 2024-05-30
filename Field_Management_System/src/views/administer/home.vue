<!-- 管理人员系统 -->
<template>
  <div class='home'>
    <el-container>
      <el-aside width="200px" class="aside">
        <h2>企业外勤管理系统</h2>
        <el-menu router active-text-color="#b7d4e1" active-background-color="#034966" background-color="#005579"
          class="aside-menu" default-active="/departmentMana" text-color="#fff">
          <el-menu-item index="/departmentMana"><el-icon>
              <House />
            </el-icon>部门管理</el-menu-item>
          <el-menu-item index="/staffList"><el-icon>
              <User />
            </el-icon>员工列表</el-menu-item>
          <el-menu-item index="/taskList"><el-icon>
              <Collection />
            </el-icon>外勤任务模块</el-menu-item>
          <el-menu-item index="/workOrderList"><el-icon>
              <Histogram />
            </el-icon>工单管理模块</el-menu-item>
          <el-menu-item index="/taskReport"><el-icon>
              <LocationFilled />
            </el-icon>外勤任务打卡管理</el-menu-item>
          <!-- <el-menu-item index="/position"><el-icon><LocationFilled /></el-icon>定位</el-menu-item> -->

        </el-menu>
      </el-aside>

      <el-container>

        <el-header class="header">
          <el-menu class="header-menu" mode="horizontal" background-color="#005579" text-color="#fff"
            active-text-color="#78e8ec">
            <el-menu-item index="1">管理人员系统</el-menu-item>
            <el-sub-menu index="2">

              <template #title>
                <el-icon>
                  <UserFilled />
                </el-icon>
                admin
              </template>
              <el-menu-item index="/personalInfo">个人中心</el-menu-item>
              <el-menu-item index="/login">退出登录</el-menu-item>
            </el-sub-menu>
          </el-menu>
        </el-header>

        <el-main class="elmain">

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
import { Edit, UserFilled, LocationFilled, List, User, Collection, Histogram, Tools, House } from '@element-plus/icons-vue'
import TpScroll from '@/utils/tp-scroll.js'
import ScrollContainer from '@c/scrollContainer'

// 移除浏览器自带的滚动条
TpScroll.RemoveScroll();
ScrollContainer;

// 滚动条触底或顶事件
const arrive = function (tb) {
  console.log(tb)
}
</script>

<style scoped lang='scss'>
.home {
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
    width: 95%;
    height: auto;
    padding: 10px 20px;
    background-color: #fff;
  }
}
</style>