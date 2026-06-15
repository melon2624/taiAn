# 全栈学习路线（前端专项）

> 面向：**3 年 Java 后端经验**，后端不再系统学习，补齐前端成为全栈  
> 配套项目：泰安 `taiAn`（Spring Boot 9005）  
> 前置文档：[bootstrap-css-learn.md](./bootstrap-css-learn.md)

---

## 目录

1. [你的起点与目标](#1-你的起点与目标)
2. [整体路线概览](#2-整体路线概览)
3. [阶段 0：CSS / Bootstrap（1～2 周）](#3-阶段-0css--bootstrap12-周)
4. [阶段 1：JavaScript 夯实（3～4 周）](#4-阶段-1javascript-夯实34-周)
5. [阶段 2：Vue 3 前端工程（4～6 周）](#5-阶段-2vue-3-前端工程46-周)
6. [阶段 3：前端工程化与部署（2～3 周）](#6-阶段-3前端工程化与部署23-周)
7. [阶段 4：进阶（按需）](#7-阶段-4进阶按需)
8. [每周时间建议](#8-每周时间建议)
9. [验收标准：什么时候算「前端够用」](#9-验收标准什么时候算前端够用)
10. [与泰安项目的练习对照表](#10-与泰安项目的练习对照表)
11. [推荐资源](#11-推荐资源)

---

## 1. 你的起点与目标

### 已有能力（不用再学）

| 领域 | 状态 |
|------|------|
| Java / Spring Boot | ✅ 3 年经验 |
| REST API 设计与实现 | ✅ 项目里已有 `UsdtController` |
| MySQL / MyBatis | ✅ 已有 |
| 跨域、Swagger | ✅ 项目已配置 |

### 需要补齐（本路线重点）

| 领域 | 现状 |
|------|------|
| HTML / CSS / Bootstrap | 🔄 正在学（见 Bootstrap 文档） |
| JavaScript | ⚠️ 项目里已有 `fetch`，需系统掌握 |
| Vue 3 + 组件化 | ❌ 待学 |
| 前端构建、部署 | ❌ 待学 |

### 全栈目标（对你而言）

```
你负责：API + 数据库 + 业务逻辑（已会）
再补齐：页面 + 交互 + 调接口 + 前端工程（本路线）
────────────────────────────────────────
= 能独立交付完整功能，不依赖前端同事改页面
```

---

## 2. 整体路线概览

```
阶段 0   CSS / Bootstrap        1～2 周    静态页能改能搭
   ↓
阶段 1   JavaScript             3～4 周    原生 JS 熟练调 API
   ↓
阶段 2   Vue 3                  4～6 周    组件化、路由、状态
   ↓
阶段 3   工程化 + 部署           2～3 周    npm 构建、对接 Spring Boot
   ↓
阶段 4   进阶（TypeScript 等）   按需
```

**总计**：业余学习约 **3～4 个月** 达到「Java 全栈 + Vue 前端」实用水平。

---

## 3. 阶段 0：CSS / Bootstrap（1～2 周）

📄 **详细内容见** [bootstrap-css-learn.md](./bootstrap-css-learn.md)

### 目标

- 能读懂、修改 `price.html`、`updatePrice.html` 的样式
- 能独立搭 landing / 价格 / 简单后台壳子

### Weekly 任务

| 周 | 任务 | 验收 |
|----|------|------|
| W1 | Day 1～4：CSS 基础 + Bootstrap 栅格 | 完成 `learn/day1`～`day4` |
| W2 | Day 5～7：表单、后台、综合页 | 完成 `learn/final/` 三个参考页 |

### 本阶段结束水平

**前端静态页初级** — 不算全栈，但是后面 JS/Vue 的基础。

---

## 4. 阶段 1：JavaScript 夯实（3～4 周）

> **配套实战文档（建议先走这条）：** [javascript-learn.md](./javascript-learn.md) — 7 天 Demo + 逐段讲解 `price.html` / `updatePrice.html`  
> 入口：http://localhost:9005/taian/learn/js

> 你项目里 `price.html`、`updatePrice.html` 已有 `fetch`，说明接触过 JS。  
> 这一阶段是 **从「能抄」到「能写、能改、能排错」**。

### 目标

- 熟练 DOM、事件、异步
- 能手写 CRUD 页面的前端逻辑（不调框架）
- 完全理解现有价格页的 JS 代码

### 你项目里已有的 API（练习用）

| 接口 | 方法 | 说明 |
|------|------|------|
| `/restapi/cai/usdtPrice` | GET | 获取展示价格 |
| `/restapi/cai/usdtPrice?price=7.78` | POST | 更新展示价格 |
| `/restapi/cai/usdtCostPrice` | GET/POST | 成本价 |

Swagger：http://localhost:9005/swagger-ui.html

---

### Week 1：语法 + DOM

**学什么**

- 变量 `let/const`、类型、模板字符串
- 函数、箭头函数
- 数组 `map/filter`、对象解构
- DOM：`getElementById`、`textContent`、`classList`
- 事件：`addEventListener`

**练什么**

1. 读透 `updatePrice.html` 每一行 JS，加注释
2. 给「保存」按钮加 **Enter 键提交**
3. 输入框只允许数字和小数点（简单校验）

**验收**

- [ ] 能解释 `document.getElementById` 和 `querySelector` 区别
- [ ] 能不用 jQuery 完成按钮点击改文字

---

### Week 2：异步 + fetch

**学什么**

- Promise、`async/await`
- `fetch` GET/POST、状态码、`response.text()` / `.json()`
- `try/catch/finally` 错误处理
- `encodeURIComponent` 拼参数

**练什么**

1. 读透 `price.html` 的 `updatePrice()` 和定时器
2. 在 `updatePrice.html` 增加 **成本价** 读写（对接 `usdtCostPrice`）
3. 统一封装一个 `api.js`：

```javascript
const API_BASE = '/restapi/cai';

export async function getPrice() {
  const res = await fetch(`${API_BASE}/usdtPrice`);
  if (!res.ok) throw new Error(`HTTP ${res.status}`);
  return res.text();
}
```

**验收**

- [ ] 能独立写一个「加载中 → 成功/失败提示」的 fetch 流程
- [ ] 能说出 `async/await` 和 `.then()` 的关系

---

### Week 3：模块化 + 表单

**学什么**

- ES Module：`import/export`（Vue 前置）
- 表单 `preventDefault`、FormData
- 本地存储 `localStorage`（可选）
- 定时器 `setInterval` / `clearInterval`

**练什么**

1. 把 `updatePrice.html` 的 JS 拆成 `api.js` + `main.js`
2. 保存成功后 3 秒自动清除提示文字
3. 给 `price.html` 加「上次更新时间」显示

**验收**

- [ ] JS 拆成多个文件仍能在浏览器跑（`<script type="module">`）
- [ ] 表单校验逻辑清晰，不依赖 HTML5  alone

---

### Week 4：小项目整合（原生 JS）

**练什么：在 `learn/` 下新建 `day-js/`**

做一个 **纯 JS 价格管理页**（不用 Vue）：

- Tab：展示价 / 成本价
- 列表区显示当前值
- 表单修改 + 保存
- 全部对接现有 Spring Boot API

**验收**

- [ ] 一个 HTML + 2～3 个 JS 文件完成
- [ ] 样式用 Bootstrap CDN
- [ ] 无框架依赖

### 本阶段结束水平

**JavaScript 初级～中级（业务向）** — 能维护现有静态页，能写简单 SPA 逻辑。

---

## 5. 阶段 2：Vue 3 前端工程（4～6 周）

> Java 开发者学 Vue 通常比 React 上手快：模板直观、文档中文友好、和 Spring Boot 组合常见。

### 目标

- 用 Vue 3 重写价格展示 + 更新功能
- 理解组件、响应式、路由
- 前端通过 axios 调你写的 API

### 环境准备（Week 5 第 1 天）

```bash
# 需安装 Node.js 18+ 和 npm
node -v
npm -v

# 在项目中新建前端目录（建议）
cd e:/code/java/taiAn
mkdir frontend
cd frontend
npm create vue@latest .
# 选项：Router 选 Yes，Pinia 选 Yes，其余按需
npm install
npm install axios
npm run dev
```

开发时配置 **代理** 解决跨域（`vite.config.js`）：

```javascript
export default defineConfig({
  server: {
    proxy: {
      '/restapi': 'http://localhost:9005'
    }
  }
})
```

---

### Week 5：Vue 基础

**学什么**

- 单文件组件 `.vue`（template / script / style）
- 响应式：`ref`、`reactive`
- 模板：`{{ }}`、`v-if`、`v-for`、`v-model`
- 组件 props / emit

**练什么**

1. 组件 `PriceDisplay.vue`：显示价格，每 5 秒刷新
2. 组件 `PriceForm.vue`：输入 + 保存

**验收**

- [ ] 能把 Day 1 的三列卡片拆成 Vue 组件
- [ ] 理解「数据变 → 页面自动变」

---

### Week 6：组合式 API + axios

**学什么**

- `<script setup>` 写法（Vue 3 推荐）
- `onMounted`、`watch`
- axios 封装 `request.js`
- loading / error 状态管理

**练什么**

```javascript
// src/api/price.js
import request from './request'

export const getUsdtPrice = () => request.get('/restapi/cai/usdtPrice')
export const setUsdtPrice = (price) =>
  request.post('/restapi/cai/usdtPrice', null, { params: { price } })
```

页面：`PriceView.vue` 整合展示 + 编辑

**验收**

- [ ] axios 拦截器统一处理错误
- [ ] 与 Spring Boot 联调成功

---

### Week 7：Vue Router

**学什么**

- 路由配置、`router-link`、`useRoute`
- 布局组件（侧边栏 + 内容区，类似 Day 6 dashboard）

**练什么**

| 路由 | 页面 |
|------|------|
| `/` | 价格展示（对应原 price.html） |
| `/admin/update` | 更新价格（对应 updatePrice.html） |
| `/admin/dashboard` | 简单统计壳子（假数据即可） |

**验收**

- [ ] 三个页面路由切换无刷新
- [ ] 共用 Layout 导航组件

---

### Week 8～9：Pinia + 整合

**学什么**

- Pinia store：全局价格状态
- 组件间共享数据
- Bootstrap 5 接入 Vue（`npm install bootstrap`）

**练什么**

1. `stores/price.js` 统一管理价格、成本价
2. 展示页和管理页共用 store
3. UI 对齐或超越现有 `price.html` 效果

**验收**

- [ ] Vue 版功能 **≥** 现有 HTML 版
- [ ] 代码结构清晰：views / components / api / stores

### 本阶段结束水平

**Vue 3 初级～中级** — 能独立做管理后台类前端，与 Java API 联调。

---

## 6. 阶段 3：前端工程化与部署（2～3 周）

### 目标

- 知道开发环境 vs 生产环境区别
- Vue 打包后接入 Spring Boot 或 Nginx
- 理解前后端分离部署基本流程

---

### Week 10：构建与接入 Spring Boot

**方案 A：打包进 Spring Boot static（简单）**

```bash
cd frontend
npm run build
# 将 dist/ 内容复制到 src/main/resources/static/app/
```

访问：http://localhost:9005/app/index.html

**方案 B：Nginx 前后端分离（更接近生产）**

- Nginx 托管 Vue 静态文件
- `/restapi` 反向代理到 Java 9005

**学什么**

- `npm run build` 产物是什么
- 环境变量 `.env.development` / `.env.production`
- 打包后路由 history 模式注意点

**验收**

- [ ] 本地 `mvn package` 后，jar 里能访问 Vue 页面
- [ ] 生产 API 地址可配置，不写死 localhost

---

### Week 11：代码质量与协作

**学什么**

- ESLint / Prettier 基本使用
- Git 分支：前端 `frontend/` 与后端同仓库
- 浏览器 Network 面板排查接口问题

**练什么**

1. 给 Vue 项目加 README（启动方式、代理配置）
2. 接口文档与 Swagger 对照，前端类型注释（可选 JSDoc）

**验收**

- [ ] 别人按 README 能跑起前后端
- [ ] 能用 F12 Network 说明一次完整请求链路

### 本阶段结束水平

**全栈交付（Java + Vue）实用级** — 能独立上线一个小系统。

---

## 7. 阶段 4：进阶（按需）

后端你已熟练，前端按需选学：

| 方向 | 内容 | 何时学 |
|------|------|--------|
| TypeScript | 类型、接口定义 | Vue 熟练后 2～3 周 |
| 单元测试 | Vitest、组件测试 | 项目变大时 |
| React | 第二框架 | 求职需要或团队技术栈 |
| 微前端 / SSR | Nuxt 等 | 业务需要时 |
| 性能优化 | 懒加载、打包分析 | 页面变慢时 |

**不建议现在就学**：React + TS + 微前端 + K8s 一起上，容易散。

---

## 8. 每周时间建议

| 投入 | 进度 |
|------|------|
| 每天 1～2 小时 | 约 4 个月走完阶段 0～3 |
| 每天 3～4 小时 | 约 2～2.5 个月 |
| 仅周末（8h/周） | 约 5～6 个月 |

---

## 9. 验收标准：什么时候算「前端够用」

### 最低标准（全栈入门）

- [ ] Bootstrap 文档 7 天完成
- [ ] 原生 JS 重写价格管理逻辑
- [ ] Vue 3 版 price + update 功能上线
- [ ] 能独立排查「接口 404 / CORS / 跨域 / 代理」问题

### 实用标准（可独立负责前端）

- [ ] 新增一个 CRUD 页面（列表 + 表单 + 分页）从 0 到联调
- [ ] 组件拆分合理，有 api / store / views 分层
- [ ] 打包部署过一次

### 你 3 年 Java + 上述前端 ≈

> **中小团队全栈开发（偏后端）** — 能一个人扛一个小模块的前后端。  
> **专职高级前端** — 还不够，需更多前端深度与工程经验。

---

## 10. 与泰安项目的练习对照表

| 学习阶段 | 在 taiAn 项目里练什么 |
|----------|----------------------|
| CSS/Bootstrap | `learn/` 7 天 + 美化 `price.html` |
| JS Week 2 | 读懂并增强 `price.html` / `updatePrice.html` |
| JS Week 4 | 新建 `learn/day-js/price-admin.html` |
| Vue Week 6～9 | `frontend/` 重写价格系统 |
| 部署 Week 10 | build 进 `static/app/` 或 Nginx |

### 现有文件 → 学完后你应该能

| 文件 | 现在 | 学完后 |
|------|------|--------|
| `price.html` | 能跑，JS 可能看不太懂 | 完全能改、能优化、能用 Vue 重写 |
| `updatePrice.html` | 同上 | 同上 |
| `UsdtController.java` | 你会写 | 不变，成为前端联调对象 |
| `learn/*` | 练习场 | 毕业可归档 |

---

## 11. 推荐资源

### JavaScript

| 资源 | 链接 |
|------|------|
| MDN JavaScript 指南 | https://developer.mozilla.org/zh-CN/docs/Web/JavaScript/Guide |
| JavaScript.info（现代教程） | https://zh.javascript.info/ |

### Vue 3

| 资源 | 链接 |
|------|------|
| Vue 3 官方文档（中文） | https://cn.vuejs.org/ |
| Vue Router | https://router.vuejs.org/zh/ |
| Pinia | https://pinia.vuejs.org/zh/ |

### 工具

| 工具 | 用途 |
|------|------|
| Node.js | 运行 npm、Vite |
| Vue DevTools | 浏览器插件，调试组件 |
| Postman / Swagger | 接口调试（你已有 Swagger） |

---

## 附录：Java 开发者学前端的「思维对照」

| Java 概念 | 前端对应 |
|-----------|----------|
| Class | Vue 组件 / JS Class |
| `@RestController` | axios 调 API |
| `@RequestParam` | `?price=7.78` 或 `{ params: { price } }` |
| DTO | TypeScript interface / JSDoc |
| Maven 依赖 | npm package |
| application.yml | `.env` / `vite.config.js` |
| 分层 controller/service | views / api / stores |

你后端越强，**联调阶段越快**；瓶颈主要在 **DOM、异步、组件化思维**，按本路线逐周突破即可。

---

## 文档索引

| 文档 | 内容 |
|------|------|
| [bootstrap-css-learn.md](./bootstrap-css-learn.md) | 阶段 0：CSS + Bootstrap 7 天 |
| **本文档** | 阶段 1～3：JS + Vue + 部署 |
| [uni-app-mobile-roadmap.md](./uni-app-mobile-roadmap.md) | 阶段 4+：iOS/Android App（Vue 学完后再学） |
| 项目练习页 | http://localhost:9005/taian/learn |

---

*最后更新：配合 taiAn 项目 bootstrap-learn 分支*  
*适用：3 年 Java 开发，目标全栈（前端补齐）*  
*再下一步（可选）：[uni-app-mobile-roadmap.md](./uni-app-mobile-roadmap.md)*
