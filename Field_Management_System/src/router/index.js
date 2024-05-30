import { createRouter, createWebHistory } from 'vue-router'
import index from '../views/login/index.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'login',
      meta: {title:'登录'},
      component: index,
    },
    {
      path: "/home",
      redirect: '/departmentMana',
      name: "home",
      meta: {title:'管理人员系统'},

      component: () => import('@v/administer/home.vue'),
      children: [
        {
          path: '/departmentMana',
          meta: {title:'部门管理'},
          component: () => import('@v/administer/department/departmentManagement.vue')
        },
        {
          path: '/staffList',
          meta: {title:'员工列表'},
          component: () => import('@v/administer/personnelManagement/staffList.vue')
        },
        {
          path: '/taskList',
          meta: {title:'外勤信息列表'},
          component: () => import('@v/administer/fieldTask/taskList.vue'),
        },
        {
          path: '/taskDetailInfo',
          meta: {title:'外勤任务详细信息'},
          component: () => import('@v/administer/fieldTask/taskDetailInfo.vue'),
        },
        {
          path: '/workOrderList',
          meta: {title:'工单列表'},
          component: () => import('@v/administer/workOrder/workOrderList.vue')
        },
        {
          path: '/workOrderDetail',
          meta: {title:'工单列表详情'},
          component: () => import('@v/administer/workOrder/workOrderDetail.vue')
        },
        {
          path: '/taskReport',
          meta: {title:'任务打卡列表'},
          component: () => import('@v/administer/system/taskReportList.vue')
        },
        {
          path: '/reportMana',
          meta: {title:'任务打卡详情'},
          component: () => import('@v/administer/system/reportDetail.vue')
        },
        {
          path:'/map',
          meta: {title:'地图'},
          component: () => import('@v/administer/system/MapContainer.vue'),
          },
          {
            path:'/map2',
            meta: {title:'地图'},
            component: () => import('@v/administer/system/map.vue'),
            },
      ]
    },
    {
      path: "/workerhome",
      redirect: '/myTaskList',
      name: "workerhome",
      meta: {title:'外勤人员系统'},
      component: () => import('@v/outworker/workerhome.vue'),
      children: [
        {
          path: '/myTaskList',
          meta: {title:'工单列表'},
          component: () => import('@v/outworker/inner/myTaskList.vue')
        },
        {
          path: '/myTaskDetail',
          meta: {title:'工单列表详情界面'},
          component: () => import('@v/outworker/inner/myTaskDetail.vue')
        },
        {
          path: '/workOrder',
          meta: {title:'工单列表'},
          component: () => import('@v/outworker/inner/workOrder.vue')
        },
        {
          path: '/report',
          meta: {title:'打卡'},
          component: () => import('@v/outworker/inner/report.vue'),
        },
        {
          path: '/message',
          meta: {title:'打卡'},
          component: () => import('@v/outworker/inner/message.vue')
        },
        {
          path: '/personalInfo',
          meta: {title:'个人中心'},
          component: () => import('@v/outworker/inner/personalInfo.vue'),
        },
        {
          path: '/location',
          meta: {title:'定位'},
          component: () => import('@v/outworker/inner/location.vue'),
        }
      ]
    }
  ]
})

export default router
