# uni-app 移动端路线（iOS + Android）

> 面向：**已完成或进行中** Web 前端学习（CSS → JS → Vue），目标开发双端 App  
> 后端：默认已会 Java / Spring Boot，本文档 **不包含后端教程**  
> 配套项目：泰安 `taiAn`（接口 `/restapi/cai/*`）

---

## 文档索引（建议学习顺序）

| 顺序 | 文档 | 内容 |
|------|------|------|
| 1 | [bootstrap-css-learn.md](./bootstrap-css-learn.md) | CSS + Bootstrap（1～2 周） |
| 2 | [fullstack-frontend-roadmap.md](./fullstack-frontend-roadmap.md) | JS + Vue 3 + Web 部署 |
| 3 | **本文档** | uni-app → iOS + Android App |

---

## 目录

1. [为什么选 uni-app](#1-为什么选-uni-app)
2. [前置要求](#2-前置要求)
3. [整体路线概览](#3-整体路线概览)
4. [环境准备](#4-环境准备)
5. [阶段 1：uni-app 入门（2 周）](#5-阶段-1uni-app-入门2-周)
6. [阶段 2：对接泰安 API（2 周）](#6-阶段-2对接泰安-api2-周)
7. [阶段 3：完整 App 功能（2～3 周）](#7-阶段-3完整-app-功能23-周)
8. [阶段 4：打包与上架（1～2 周）](#8-阶段-4打包与上架12-周)
9. [Vue 与 uni-app 差异速查](#9-vue-与-uni-app-差异速查)
10. [验收标准](#10-验收标准)
11. [常见问题](#11-常见问题)
12. [推荐资源](#12-推荐资源)

---

## 1. 为什么选 uni-app

| 对比 | uni-app | Flutter | 原生 Swift+Kotlin |
|------|---------|---------|-------------------|
| 语言 | Vue 语法 | Dart | 两套语言 |
| 与现有学习路线 | ✅ 衔接 Vue | ❌ 另起炉灶 | ❌ 工作量 ×2 |
| 一套代码双端 | ✅ | ✅ | ❌ |
| 国内资料 / 小程序 | ✅ 可顺带出小程序 | 一般 | 需单独做 |
| 适合你的情况 | **推荐** | 备选 | 不推荐起步 |

你已计划学 **Vue**，uni-app 是 **Vue 语法 + 移动端组件 + 打包工具**，迁移成本最低。

---

## 2. 前置要求

开始本文档前，建议至少达到：

| 能力 | 最低要求 |
|------|----------|
| HTML / CSS 基础 | 完成 Bootstrap 文档 Day 1～2 |
| JavaScript | 会 `async/await`、`fetch`、函数、数组 |
| Vue 3 | 会 `ref`、`v-model`、组件、`onMounted`（见全栈文档阶段 2） |

**可并行**：全栈文档学到 Vue Week 5 后，即可开始 uni-app 阶段 1（边学 Vue 边接触 uni-app 也行，但稍吃力）。

**后端**：能启动 `taiAn`、知道 Swagger 地址即可。

```
GET  /restapi/cai/usdtPrice
POST /restapi/cai/usdtPrice?price=7.78
GET  /restapi/cai/usdtCostPrice
POST /restapi/cai/usdtCostPrice?price=7.75
```

Swagger：http://localhost:9005/swagger-ui.html

---

## 3. 整体路线概览

```
前置：CSS + JS + Vue（见前两份文档）
        ↓
阶段 1   uni-app 入门           2 周     页面、路由、基础组件
        ↓
阶段 2   对接泰安 API            2 周     展示价、改价、错误处理
        ↓
阶段 3   完整 App                2～3 周   Tab、下拉刷新、本地存储
        ↓
阶段 4   打包 iOS / Android      1～2 周   真机、证书、上架流程
```

**总计**：在前置 Vue 基础上，业余约 **2～3 个月** 能做出可内测/上架的简单双端 App。

---

## 4. 环境准备

### 必装软件

| 工具 | 用途 | 下载 |
|------|------|------|
| **HBuilderX** | uni-app 官方 IDE，创建/运行/打包 | https://www.dcloud.io/hbuilderx.html |
| **Node.js 18+** | 可选，CLI 方式创建项目时需要 | https://nodejs.org |
| **微信开发者工具** | 调试小程序版（可选） | 微信官网 |

### 打包 iOS / Android 额外需要

| 平台 | 需要 |
|------|------|
| **Android** | Android Studio（SDK）、真机或模拟器；云打包可不装本地 SDK |
| **iOS** | macOS + Xcode（真机调试/上架 **必须 Mac**）；Windows 只能用云打包出 ipa，上架仍要 Mac |

> 没有 Mac：Android 可完整走通；iOS 可用 DCloud 云打包 + 借 Mac 上架，或先专注 Android。

### 创建项目（HBuilderX）

1. 文件 → 新建 → 项目 → **uni-app**
2. 选默认模板或 **uni-app x**（建议先经典 **Vue3 + Vite** 模板）
3. 项目建议目录：`e:/code/java/taiAn/mobile/`（与 Java 后端同仓库，便于管理）

### 开发时连本地 Java 后端

**方式 1：真机 / 模拟器访问电脑 IP**

```javascript
// utils/config.js
// 开发：改成你电脑的局域网 IP，不要用 localhost
export const API_BASE = 'http://192.168.1.100:9005/restapi/cai'
```

**方式 2：H5 运行 + 浏览器**

H5 运行时可用 `http://localhost:9005`（仅浏览器调试方便）。

**注意**：真机访问需 Spring Boot 监听 `0.0.0.0`，且手机与电脑同一 WiFi；生产环境改 HTTPS 域名。

---

## 5. 阶段 1：uni-app 入门（2 周）

### 目标

- 熟悉 uni-app 项目结构
- 会用常用组件和页面跳转
- 理解「一个 Vue 页面 = App 的一屏」

### 项目结构（了解即可）

```
mobile/
├── pages/              # 页面
│   ├── index/index.vue
│   └── mine/mine.vue
├── static/             # 静态资源
├── App.vue             # 应用入口
├── main.js
├── pages.json          # 页面路由 + 导航栏配置（重要）
└── manifest.json       # App 配置、权限、打包
```

---

### Week 1：组件 + 布局

**学什么**

| 类别 | API / 组件 |
|------|------------|
| 布局 | `view`（≈ div）、`text`、`image` |
| 列表 | `scroll-view` |
| 样式 | rpx 单位（750rpx = 屏宽）、flex 布局（与 CSS 相同） |
| 交互 | `@click`、`v-model`（input） |

**rpx 要点**

```
750rpx = 屏幕全宽
375rpx ≈ 半屏宽
```

比 px 更适合不同手机宽度。

**练什么**

1. 首页：标题「泰安 USDT」+ 大号占位「--.--」+ 单位「HKD」
2. 样式参考 `price.html` 黑金风格（练手即可）
3. 底部放两个按钮（暂不请求接口）

**验收**

- [ ] 能在 HBuilderX 运行到浏览器（H5）或模拟器
- [ ] 理解 `view` / `text` 与 HTML 标签对应关系
- [ ] 会用 flex 做垂直居中

---

### Week 2：路由 + TabBar

**学什么**

- `pages.json` 注册页面、设导航栏标题
- `uni.navigateTo` / `uni.switchTab`
- 底部 **TabBar**（首页 / 管理 / 我的）

**pages.json 示例片段**

```json
{
  "pages": [
    { "path": "pages/index/index", "style": { "navigationBarTitleText": "今日价格" } },
    { "path": "pages/admin/admin", "style": { "navigationBarTitleText": "更新价格" } }
  ],
  "tabBar": {
    "list": [
      { "pagePath": "pages/index/index", "text": "价格" },
      { "pagePath": "pages/admin/admin", "text": "管理" }
    ]
  }
}
```

**练什么**

1. 三个 Tab 页：价格展示、价格管理、关于
2. 页面之间跳转正常
3. 导航栏标题与 Web 版对应

**验收**

- [ ] TabBar 三页可切换
- [ ] 能说出 `pages.json` 和 Vue Router 的相似点（都是「路径 → 页面」）

---

## 6. 阶段 2：对接泰安 API（2 周）

### 目标

- 用 `uni.request` 调 Spring Boot 接口
- 完成「展示价 + 改价」核心闭环
- 处理好 loading / 错误 / 无网络

---

### Week 3：网络请求封装

**学什么**

- `uni.request`（类似 axios / fetch）
- Promise 封装、统一 baseURL
- 开发环境 IP 配置

**api/price.js 示例**

```javascript
import { API_BASE } from '@/utils/config.js'

function request(url, options = {}) {
  return new Promise((resolve, reject) => {
    uni.request({
      url: API_BASE + url,
      method: options.method || 'GET',
      data: options.data,
      success: (res) => {
        if (res.statusCode >= 200 && res.statusCode < 300) {
          resolve(res.data)
        } else {
          reject(new Error('HTTP ' + res.statusCode))
        }
      },
      fail: reject
    })
  })
}

export const getUsdtPrice = () => request('/usdtPrice')
export const setUsdtPrice = (price) =>
  request('/usdtPrice?price=' + encodeURIComponent(price), { method: 'POST' })
export const getUsdtCostPrice = () => request('/usdtCostPrice')
export const setUsdtCostPrice = (price) =>
  request('/usdtCostPrice?price=' + encodeURIComponent(price), { method: 'POST' })
```

**练什么**

1. 首页 `onShow` 时请求 `getUsdtPrice`，显示在页面
2. 管理页：输入框 + 保存，调 `setUsdtPrice`
3. 请求失败时 `uni.showToast({ title: '网络错误', icon: 'none' })`

**验收**

- [ ] 真机或模拟器能访问电脑上的 Java 服务
- [ ] 保存成功后首页价格能更新（返回 Tab 刷新或手动刷新）

---

### Week 4：体验优化

**学什么**

- `uni.showLoading` / `uni.hideLoading`
- 下拉刷新 `onPullDownRefresh`（在 pages.json 开启）
- 定时刷新（`setInterval`，注意页面销毁时 `clearInterval`）
- 本地缓存 `uni.setStorageSync` 存上次价格（离线显示）

**练什么**

1. 首页每 5 秒自动刷新价格（对齐 `price.html`）
2. 下拉刷新手动更新
3. 管理页增加 **成本价** 读写

**验收**

- [ ] loading 不会卡死（finally 里 hideLoading）
- [ ] 与 Web 版 `updatePrice.html` 功能对齐

---

## 7. 阶段 3：完整 App 功能（2～3 周）

### 目标

- UI 接近可发布
- 常见 App 交互齐全
- 代码分层清晰

---

### Week 5～6：UI 与结构

**学什么**

- uni-ui 或 uView 组件库（任选其一，加速 UI）
- 表单校验、数字键盘
- 深色主题（可选，对齐 price 页）

**推荐目录**

```
mobile/
├── pages/
├── api/
│   └── price.js
├── utils/
│   └── config.js
├── components/
│   └── PriceDisplay.vue
└── stores/          # 若用 Pinia，与 Vue 相同
```

**练什么**

1. 抽组件 `PriceDisplay`（大号价格 + 更新时间）
2. 「关于」页：公司名、WhatsApp、地址（复制自 price.html）
3. 统一主题色、间距

**验收**

- [ ] 组件可在多页复用
- [ ] 无明显「网页感」，像 App

---

### Week 7（可选）：进阶功能

按需选做：

| 功能 | uni API |
|------|---------|
| 复制 WhatsApp 号 | `uni.setClipboardData` |
| 拨打电话 | `uni.makePhoneCall` |
| 分享 | `uni.share`（需配置） |
| 启动图 / 图标 | `manifest.json` + `static/` |

**验收**

- [ ] 点击 WhatsApp 可复制号码
- [ ] App 图标、名称在 manifest 里已配置

---

## 8. 阶段 4：打包与上架（1～2 周）

### 目标

- Android 打出 apk/aab 并真机安装
- 了解 iOS 打包与上架流程
- 知道审核常见坑

---

### Week 8：Android 打包

**步骤概要**

1. `manifest.json` → App 模块：填包名、版本号、权限
2. 图标：准备 1024 及各尺寸（HBuilderX 可自动生成）
3. **云打包**（入门推荐）：HBuilderX → 发行 → 原生 App-云打包
4. 或本地打包：配置 Android Studio SDK

**权限注意**

- 仅 HTTP 调接口：网络权限即可
- 上架 Google Play：targetSdk 按 HBuilderX 提示，隐私政策 URL 可能需要

**验收**

- [ ] apk 安装到 Android 手机
- [ ] 能打开 App 并正常显示价格

---

### Week 9：iOS 打包（有 Mac 时）

**步骤概要**

1. Apple Developer 账号（99 USD/年）
2. 证书、描述文件（HBuilderX 云打包可协助）
3. 云打包或 Xcode 真机调试
4. App Store Connect 提交审核

**无 Mac**

- 可先完成 Android + H5 + 小程序
- iOS 云打包出 ipa 后，仍需 Mac + Transporter 上传（或借 Mac）

**验收**

- [ ] 了解完整流程（即使暂未上架）
- [ ] TestFlight 或真机安装成功（可选）

---

### 上架 checklist

- [ ] App 名称、图标、启动图
- [ ] 隐私政策链接（收集数据时必需）
- [ ] 应用截图（各尺寸）
- [ ] 版本号 `versionName` / `versionCode`
- [ ] 生产 API 改为 **HTTPS** 域名（商店常要求）

---

## 9. Vue 与 uni-app 差异速查

| Web (Vue 3) | uni-app |
|-------------|---------|
| `<div>` | `<view>` |
| `<span>` / 文本 | `<text>` |
| `<img>` | `<image src="">` |
| `axios.get()` | `uni.request()` |
| `vue-router` | `pages.json` + `uni.navigateTo` |
| `localStorage` | `uni.setStorageSync` |
| `px` / `rem` | 优先 **rpx** |
| `window` DOM | 无 DOM，不操作 `document` |
| `@click` | `@click`（相同） |
| `v-model` | `v-model`（相同） |
| Pinia | 可用 Pinia（需配置） |

**记住**：uni-app 里 **不要** 用 `document.getElementById`，一切用 Vue 数据驱动。

---

## 10. 验收标准

### 最低标准（能用的双端 App）

- [ ] 首页展示 USDT 价格，可定时/下拉刷新
- [ ] 管理页可修改展示价（+ 成本价更佳）
- [ ] 对接现有 Spring Boot，无 mock
- [ ] Android apk 真机可安装运行

### 实用标准（可给别人用 / 上架）

- [ ] Tab 结构清晰，UI 统一
- [ ] 网络错误、loading 体验完整
- [ ] 生产环境 HTTPS + 正式域名
- [ ] 至少一端（Android 或 iOS）走通打包上架流程

### 学完后你是什么水平

> **uni-app 初级～中级（业务 App）**  
> 能独立做「表单 + 列表 + 调 Java API」类双端 App；  
> **不是** 原生 iOS/Android 专家，复杂动画/蓝牙/AR 等需另学。

---

## 11. 常见问题

### Q：真机访问不了 localhost:9005？

电脑 IP + 同一 WiFi；Spring Boot 绑定 `0.0.0.0`；防火墙放行 9005。

### Q：和 Web 版 Vue 项目要分开吗？

建议分开：`frontend/`（Web 管理台）+ `mobile/`（uni-app），共用同一套 Java API。

### Q：能否先不做 App，只做 uni-app 的 H5？

可以。H5 运行最快，适合前期只练请求和页面；双端打包后面再做。

### Q：uni-app 和 Flutter 怎么选？

你已走 Vue 线 → **uni-app**。若团队指定 Flutter 或追求复杂动效，再学 Flutter。

### Q：后端要改吗？

开发期注意 CORS（你项目 `UsdtController` 已有 `@CrossOrigin`）；上线建议 HTTPS + 鉴权（更新价格接口应加登录，后端你自行处理）。

---

## 12. 推荐资源

| 资源 | 链接 |
|------|------|
| uni-app 官方文档 | https://uniapp.dcloud.net.cn/ |
| uni-app Vue3 教程 | 官方「教程」栏目 |
| pages.json 配置 | https://uniapp.dcloud.net.cn/collocation/pages.html |
| uni.request | https://uniapp.dcloud.net.cn/api/request/request.html |
| DCloud 问答社区 | https://ask.dcloud.net.cn/ |
| uni-ui 组件库 | https://uniapp.dcloud.net.cn/component/uniui/uni-ui.html |

---

## 附录：与泰安项目对照

| Web 页面 | App 页面 |
|----------|----------|
| `price.html` | `pages/index/index.vue` |
| `updatePrice.html` | `pages/admin/admin.vue` |
| `UsdtController` | 不变，App 只调 API |

**建议仓库结构（学完后）**

```
taiAn/
├── src/                 # Java 后端
├── docs/                # 三份学习文档
├── frontend/            # Vue Web（全栈文档）
└── mobile/              # uni-app（本文档）
```

---

## 三份文档关系图

```
bootstrap-css-learn.md
        │  CSS / 布局 / Bootstrap
        ▼
fullstack-frontend-roadmap.md
        │  JavaScript / Vue 3 / Web 部署
        ▼
uni-app-mobile-roadmap.md  ← 你在这里（可以后再学）
        │  iOS + Android App
        ▼
   可交付：Java API + Web 管理 + 双端 App
```

---

*文档路径：`docs/uni-app-mobile-roadmap.md`*  
*前置：[fullstack-frontend-roadmap.md](./fullstack-frontend-roadmap.md)*  
*适用：3 年 Java 后端 + Vue 路线，目标 iOS/Android App*
