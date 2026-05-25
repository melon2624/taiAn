# Bootstrap + CSS 快速学习指南

> 泰安项目（taiAn）配套文档 · 约 1～2 周上手  
> 当前分支建议：`bootstrap-learn`

---

## 目录

1. [学习前准备](#1-学习前准备)
2. [先分清学什么](#2-先分清学什么)
3. [项目文件结构](#3-项目文件结构)
4. [第 1 步：CSS 基础（2～3 天）](#4-第-1-步css-基础23-天)
5. [7 天实战路线](#5-7-天实战路线)
6. [常用 Bootstrap 类名速查](#6-常用-bootstrap-类名速查)
7. [学习技巧与常见误区](#7-学习技巧与常见误区)
8. [验收清单](#8-验收清单)
9. [推荐外部资源](#9-推荐外部资源)

---

## 1. 学习前准备

### 启动项目

```bash
# 在项目根目录运行 Spring Boot
mvn spring-boot:run
# 或在 IDE 中运行 TaiAnApplication.java
```

默认端口：**9005**

### 访问地址

| 页面 | 地址 |
|------|------|
| 学习中心首页 | http://localhost:9005/taian/learn |
| Day 1 练习 | http://localhost:9005/learn/day1/practice.html |
| Day 2 练习 | http://localhost:9005/learn/day2/responsive.html |
| 业务价格页 | http://localhost:9005/taian/price |
| 业务更新页 | http://localhost:9005/taian/updatePrice |

### 必备工具

- **浏览器 F12**：Elements 面板查看样式，切换手机模式测响应式
- **编辑器**：同时打开 HTML + CSS 文件对照修改
- **刷新页面**：改 CSS 后 Ctrl+F5 强制刷新

---

## 2. 先分清学什么

| 内容 | 作用 |
|------|------|
| **CSS** | 布局、颜色、间距、响应式的**基础** |
| **Bootstrap** | 基于 CSS 的**组件库**（按钮、栅格、导航等），帮你少写 CSS |

**结论：先会 CSS 基础，再用 Bootstrap。**

否则只会 copy 类名，改间距、对齐、移动端时容易卡住。

```
CSS 基础  →  知道「为什么这样布局」
Bootstrap  →  用现成 class 快速搭页面
```

---

## 3. 项目文件结构

```
src/main/resources/static/learn/
├── index.html              # 学习中心首页（7 天路线图）
├── css/
│   ├── day1.css            # Day1 纯 CSS 样式
│   ├── day2.css            # Day2 响应式（含 TODO）
│   └── custom.css          # Day6+ 自定义样式
├── day1/practice.html      # Day1 练习页
├── day2/responsive.html    # Day2 练习页
├── day3/index.html         # Bootstrap 导航 + Hero
├── day4/features.html      # 栅格 + 卡片
├── day5/pricing.html       # 表单 + 价格页
├── day6/dashboard.html     # 后台布局
└── final/                  # Day7 参考成品
    ├── index.html
    ├── pricing.html
    └── dashboard.html
```

CSS 引入顺序（Bootstrap 页面）：

```html
<link href="bootstrap CDN" rel="stylesheet">
<link href="/learn/css/custom.css" rel="stylesheet">  <!-- 放后面才能覆盖 -->
```

---

## 4. 第 1 步：CSS 基础（2～3 天）

> 目标：能看懂样式、能改布局、能写简单页面。  
> 对应文件：`learn/css/day1.css`、`learn/day1/practice.html`

### 4.1 选择器（半天）

知道「改谁」：

```css
.card { }           /* class：最常用 */
#logo { }           /* id：一个页面尽量只用一次 */
nav a { }           /* 后代：nav 里的所有 a 标签 */
nav a:hover { }     /* 伪类：鼠标悬停 */
.card h3 { }        /* 卡片里的 h3 标题 */
```

**练习**：打开 `day1.css`，找到 `.site-nav a:hover`，把颜色改成 `#e11d48`（红色），保存后刷新页面看效果。

---

### 4.2 盒模型（半天）

每个元素都是一个「盒子」：

```
┌─ margin（外边距，盒子与盒子之间）
│  ┌─ border（边框）
│  │  ┌─ padding（内边距，文字与边框之间）
│  │  │  内容 content
```

| 属性 | 作用 | 示例 |
|------|------|------|
| `padding` | 内边距 | `padding: 24px` |
| `margin` | 外边距 | `margin: 0 auto`（块级居中） |
| `border` | 边框 | `border: 1px solid #eee` |
| `width` | 宽度 | `max-width: 1200px` |

**必加**（避免宽度计算溢出）：

```css
* {
  box-sizing: border-box;
}
```

**练习**：

1. F12 选中 `.card` → 看 Computed 面板里的 box model 图
2. 在 `day1.css` 里把 `.card` 的 `padding` 从 `24px` 改成 `32px`，观察变化

---

### 4.3 Flex 布局（1 天，最重要）

现代页面 **80% 布局靠 Flex**。项目 `day1.css` 里导航和三列卡片都是 Flex。

```css
.parent {
  display: flex;                    /* 开启 Flex */
  justify-content: space-between;     /* 主轴：左右两端对齐 */
  align-items: center;                /* 交叉轴：垂直居中 */
  gap: 20px;                          /* 子元素间距 */
}

.child {
  flex: 1;                            /* 子元素等分剩余空间 */
}
```

#### 常用场景对照

| 场景 | CSS |
|------|-----|
| 导航 Logo 左、菜单右 | 父：`display:flex; justify-content:space-between; align-items:center` |
| 内容水平垂直居中 | 父：`display:flex; justify-content:center; align-items:center` |
| 三列等宽并排 | 父：`display:flex; gap:20px` + 子：`flex:1` |
| 窄屏改单列 | 父：`flex-direction:column`（Day 2） |

#### 项目中的实际代码

**导航栏**（`.site-header`）：

```css
.site-header {
  display: flex;
  justify-content: space-between;  /* Logo 左，菜单右 */
  align-items: center;               /* 垂直居中 */
  padding: 16px 24px;
}
```

**三列卡片**（`.card-row` + `.card`）：

```css
.card-row {
  display: flex;
  gap: 20px;
  max-width: 1200px;
  margin: 0 auto;    /* 整体水平居中 */
}

.card {
  flex: 1;           /* 三列等宽 */
}
```

**练习**：访问 Day 1 页面，对照 CSS 文件逐段阅读，理解每行作用。

---

### 4.4 文字与颜色（2 小时）

```css
font-family: sans-serif;       /* 字体族 */
font-size: 1.1rem;             /* 字号（rem = 相对根元素） */
font-weight: bold;             /* 粗细：normal / bold / 600 */
color: #333;                   /* 文字颜色 */
background: #fff;              /* 背景色 */
line-height: 1.6;              /* 行高，影响可读性 */
border-radius: 8px;            /* 圆角 */
box-shadow: 0 2px 8px rgba(0,0,0,0.08);  /* 阴影 */
text-decoration: none;         /* 去掉链接下划线 */
transition: color 0.2s;        /* 颜色变化动画 */
```

**练习：给卡片加 hover 效果**

在 `day1.css` 末尾添加：

```css
.card {
  transition: transform 0.2s, box-shadow 0.2s;
}

.card:hover {
  transform: translateY(-4px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.12);
}
```

保存 → 刷新 → 鼠标移到卡片上看效果。

---

### 4.5 响应式入门（Day 2，约半天）

#### viewport（HTML 里必须有）

```html
<meta name="viewport" content="width=device-width, initial-scale=1.0">
```

#### 媒体查询

```css
@media (max-width: 768px) {
  .card-row {
    flex-direction: column;   /* 窄屏：横向改纵向，三列变一列 */
    padding: 24px 16px;
  }
}
```

**练习**：

1. 打开 `learn/css/day2.css`
2. 取消 `@media` 块的注释（删除 `/*` 和 `*/`）
3. 访问 http://localhost:9005/learn/day2/responsive.html
4. F12 → 切换设备工具栏 → 选 iPhone（375px）→ 看三列是否变一列

---

### 4.6 CSS 基础 3 天安排

| 天 | 任务 | 文件 |
|----|------|------|
| 第 1 天 | 选择器 + 盒模型 + Flex，读懂 Day 1 页面 | `day1/practice.html` + `css/day1.css` |
| 第 2 天 | 加 `@media`，手机变单列 | `day2/responsive.html` + `css/day2.css` |
| 第 3 天 | **不看教程**，从零手写：导航 + 3 卡片 + 页脚 | 新建 `day1/my-page.html` |

---

## 5. 7 天实战路线

### 最终目标

做出 **SaaS 产品官网 + 价格页 + 后台概览**，覆盖 Bootstrap 和 CSS 约 80% 常用知识点。

| 页面 | 文件 | 作用 |
|------|------|------|
| 首页 | `final/index.html` | 营销落地页 |
| 价格页 | `final/pricing.html` | 三档价格对比 |
| 后台 | `final/dashboard.html` | 管理界面 |

---

### Day 1 · 纯 CSS 基础

- **页面**：`/learn/day1/practice.html`
- **样式**：`/learn/css/day1.css`
- **知识点**：选择器、盒模型、Flex、文字颜色、阴影
- **任务**：
  - [ ] 读懂 `day1.css` 每个选择器
  - [ ] 改品牌名（CloudNote → 你自己的名字）
  - [ ] 给 `.card` 加 hover 效果
  - [ ] F12 查看盒模型

---

### Day 2 · 响应式

- **页面**：`/learn/day2/responsive.html`
- **样式**：`/learn/css/day2.css`
- **知识点**：`@media`、`viewport`、`flex-direction`
- **任务**：
  - [ ] 取消 `day2.css` 里 `@media` 注释
  - [ ] F12 测试 375px / 768px / 1200px 三种宽度
  - [ ] 理解「宽屏三列、窄屏一列」原理

---

### Day 3 · Bootstrap 入门

- **页面**：`/learn/day3/index.html`
- **知识点**：CDN 引入、Navbar、Container、Button、间距类
- **必记 class**：

```
container, navbar, navbar-brand, nav-link
btn, btn-primary, btn-lg
py-5, mb-3, text-center
bg-primary, text-white, text-muted
```

- **任务**：
  - [ ] 补全 Hero 区的大按钮（页面里有 TODO 注释）
  - [ ] 窄屏下导航出现汉堡菜单（☰）
  - [ ] 理解 `navbar-expand-lg` 含义

**Hero 区参考结构**：

```html
<section class="bg-primary text-white py-5">
  <div class="container text-center">
    <h1 class="display-4 fw-bold">云端笔记，随时随地</h1>
    <p class="lead mt-3">团队协作 · 安全加密 · 实时同步</p>
    <a href="#" class="btn btn-light btn-lg mt-3">免费开始</a>
  </div>
</section>
```

---

### Day 4 · 栅格 + Card

- **页面**：`/learn/day4/features.html`
- **知识点**：`row`、`col-md-4`、`card`、`g-4`、`h-100`
- **任务**：
  - [ ] 在 `row` 内添加 3 个 `col-md-4` 卡片
  - [ ] 桌面 3 列，手机自动 1 列
  - [ ] 卡片等高（`h-100`）

**卡片模板**：

```html
<div class="col-md-4">
  <div class="card h-100">
    <div class="card-body text-center">
      <h5 class="card-title">功能名</h5>
      <p class="card-text text-muted">描述文字</p>
    </div>
  </div>
</div>
```

**理解**：`col-md-4` = 屏幕 ≥768px 时占 12 栅格中的 4 格（即 1/3 宽）。

---

### Day 5 · 表单 + 价格页

- **页面**：`/learn/day5/pricing.html`
- **知识点**：`form-control`、`form-label`、`badge`、`list-unstyled`
- **任务**：
  - [ ] 完成三档价格卡片（基础 / 专业 / 企业）
  - [ ] 中间档加 `badge`「推荐」并高亮边框
  - [ ] 底部邮件订阅表单

**价格卡片要点**：

```html
<div class="col-md-4">
  <div class="card h-100 border-primary shadow">  <!-- 推荐档加 border-primary -->
    <span class="badge bg-primary">推荐</span>
    <div class="card-body text-center">
      <h5>专业版</h5>
      <p class="display-6">¥29</p>
      <ul class="list-unstyled">
        <li>100GB 空间</li>
        <li>团队协作</li>
      </ul>
      <a href="#" class="btn btn-primary">立即订阅</a>
    </div>
  </div>
</div>
```

**表单模板**：

```html
<form class="row g-2">
  <div class="col-md-8">
    <input type="email" class="form-control" placeholder="your@email.com">
  </div>
  <div class="col-md-4">
    <button type="submit" class="btn btn-primary w-100">订阅</button>
  </div>
</form>
```

---

### Day 6 · 后台布局

- **页面**：`/learn/day6/dashboard.html`
- **样式**：`/learn/css/custom.css`
- **知识点**：侧边栏、`table`、`table-striped`、自定义 CSS 覆盖 Bootstrap
- **任务**：
  - [ ] 理解 `col-md-2` + `col-md-10` 左右布局
  - [ ] 表格加 2 行数据
  - [ ] 修改 `.sidebar` 背景色

**侧边栏自定义 CSS**（已在 `custom.css`）：

```css
.sidebar {
  min-height: 100vh;
  background: #212529;
}
.sidebar a {
  color: #adb5bd;
  display: block;
  padding: 10px 16px;
}
.sidebar a:hover,
.sidebar a.active {
  background: #343a40;
  color: #fff;
}
```

**表格**：

```html
<table class="table table-striped table-hover">
  <thead>
    <tr><th>ID</th><th>用户名</th><th>状态</th></tr>
  </thead>
  <tbody>
    <tr>
      <td>1</td>
      <td>张三</td>
      <td><span class="badge bg-success">正常</span></td>
    </tr>
  </tbody>
</table>
```

---

### Day 7 · 综合实战

- **参考页**：`/learn/final/` 下三个 HTML
- **任务**（不看教程独立完成）：
  - [ ] 品牌名改成自己的（如「TaiAn 价格系统」）
  - [ ] 三个页面 Navbar 统一且能互相跳转
  - [ ] 首页加 FAQ 折叠面板（Accordion）
  - [ ] 统一配色（改 `custom.css` 里 `--brand-primary`）
  - [ ] 手机端自测三遍

**Accordion 示例**：

```html
<div class="accordion" id="faq">
  <div class="accordion-item">
    <h2 class="accordion-header">
      <button class="accordion-button" type="button"
              data-bs-toggle="collapse" data-bs-target="#q1">
        如何开始？
      </button>
    </h2>
    <div id="q1" class="accordion-collapse collapse show" data-bs-parent="#faq">
      <div class="accordion-body">注册账号即可免费使用。</div>
    </div>
  </div>
</div>
```

> 需要 Bootstrap JS：`<script src="bootstrap.bundle.min.js"></script>`

---

### 7 天知识点地图

| 天 | CSS | Bootstrap |
|----|-----|-----------|
| 1 | 选择器、盒模型、Flex | — |
| 2 | @media、rem、max-width | — |
| 3 | — | Container、Navbar、Button、spacing |
| 4 | gap 概念 | Grid、Card |
| 5 | 少量自定义 | Form、Badge、List |
| 6 | sidebar 自定义样式 | Table、layout 组合 |
| 7 | CSS 变量 | Accordion、整体整合 |

---

## 6. 常用 Bootstrap 类名速查

### 布局

| Class | 作用 |
|-------|------|
| `container` | 固定最大宽度居中容器 |
| `container-fluid` | 全宽容器 |
| `row` | 栅格行 |
| `col-md-4` | ≥768px 时占 4/12 宽 |
| `g-4` | 列间距（gap） |
| `d-flex` | display:flex |
| `justify-content-center` | 主轴居中 |
| `align-items-center` | 交叉轴居中 |

### 间距

| Class | 作用 |
|-------|------|
| `m-3` | margin 四周 |
| `mt-3` / `mb-3` | 上 / 下 margin |
| `p-4` / `py-5` | padding |
| `mx-auto` | 水平居中 |

### 文字与颜色

| Class | 作用 |
|-------|------|
| `text-center` | 文字居中 |
| `text-muted` | 灰色次要文字 |
| `text-white` | 白色文字 |
| `bg-primary` | 主题色背景 |
| `fw-bold` | 加粗 |
| `lead` | Lead 段落字号 |

### 组件

| Class | 作用 |
|-------|------|
| `btn btn-primary` | 主按钮 |
| `btn btn-outline-primary` | 线框按钮 |
| `card` / `card-body` | 卡片 |
| `navbar` / `nav-link` | 导航 |
| `form-label` / `form-control` | 表单 |
| `table table-striped` | 斑马纹表格 |
| `badge bg-success` | 状态标签 |

---

## 7. 学习技巧与常见误区

### 高效技巧

1. **边写边看**：改一行 CSS → 保存 → 刷新，立刻看变化
2. **F12 开发者工具**：右键「检查」→ Styles / Computed，这是学 CSS 最快的方式
3. **先抄再改**：Bootstrap 页面先 copy 能跑，再改颜色、间距、列数
4. **每天固定节奏**：写 30 分钟 → F12 看 10 分钟 → 改一个细节 10 分钟

### 常见误区（避开能快一倍）

| 误区 | 正确做法 |
|------|----------|
| 先学动画、渐变、复杂选择器 | 先掌握 Flex + 盒模型 |
| 死记所有 Bootstrap class | 常用 20 个够用，其余查文档 |
| 跳过 Flex 直接上 Bootstrap 栅格 | 先理解 Flex，再看 col-md-4 |
| 单独替换某个 Qt/Bootstrap 文件 | Bootstrap 组件要整套版本一致 |

### 样式不对时排查顺序

1. class 名是否拼错？
2. 父元素是否是 `display:flex` 或 `row`？
3. 自定义 CSS 是否在 Bootstrap **之后**引入？
4. 浏览器缓存？试 Ctrl+F5

---

## 8. 验收清单

### CSS 基础（Day 1～2 完成后）

- [ ] 能说出 `padding` 和 `margin` 的区别
- [ ] 会用 `display: flex` 做横向排列
- [ ] 会用 `flex: 1` 做等宽三列
- [ ] 会用 F12 看元素盒模型
- [ ] 会写 `@media` 让手机变单列

### Bootstrap 实战（Day 7 完成后）

- [ ] 会用 Bootstrap Grid（`row` / `col-*`）
- [ ] 会用 Navbar、Card、Button、Form、Table
- [ ] 会用间距/文字工具类（`mt-3`、`text-center`）
- [ ] 会在 Bootstrap 基础上写自定义 CSS
- [ ] 能独立做一个完整落地页 + 价格页

---

## 9. 推荐外部资源

| 资源 | 链接 | 说明 |
|------|------|------|
| MDN CSS 入门 | https://developer.mozilla.org/zh-CN/docs/Learn/CSS | Flexbox 章节优先 |
| Bootstrap 5.3 文档 | https://getbootstrap.com/docs/5.3/getting-started/introduction/ | 官方文档 |
| Bootstrap 示例 | https://getbootstrap.com/docs/5.3/examples/ | 直接改官方示例 |
| Bootstrap Icons | https://icons.getbootstrap.com/ | 可选图标 |

---

## 附录：每日固定动作

```
1. 打开学习中心 → 点当天练习链接
2. 编辑器打开对应 HTML + CSS
3. 按文档任务逐项完成
4. F12 验证效果
5. 打勾验收清单
6. 截图保存对比（可选）
```

---

*文档路径：`docs/bootstrap-css-learn.md`*  
*配合项目路径：`src/main/resources/static/learn/`*
