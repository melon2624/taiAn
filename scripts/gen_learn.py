# -*- coding: utf-8 -*-
import os

BASE = os.path.join(os.path.dirname(__file__), "..", "src", "main", "resources", "static", "learn")
D = "div"

def w(rel, html):
    path = os.path.join(BASE, rel.replace("/", os.sep))
    os.makedirs(os.path.dirname(path), exist_ok=True)
    with open(path, "w", encoding="utf-8") as f:
        f.write(html)
    print("OK", rel)

BS = "https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
JS = "https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
LOCAL_BS = "/learn/vendor/bootstrap/css/bootstrap.css"
LOCAL_JS = "/learn/vendor/bootstrap/js/bootstrap.bundle.js"

def bs_head(title):
    return f"""<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>{title}</title>
  <link href="{BS}" rel="stylesheet">
  <link href="/learn/css/custom.css" rel="stylesheet">
</head>
<body>"""

def nav_links():
    return f"""
  <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <{D} class="container">
      <a class="navbar-brand" href="/learn/index.html">CloudNote</a>
      <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navMain">
        <span class="navbar-toggler-icon"></span>
      </button>
      <{D} class="collapse navbar-collapse" id="navMain">
        <ul class="navbar-nav ms-auto">
          <li class="nav-item"><a class="nav-link" href="/learn/index.html">学习中心</a></li>
          <li class="nav-item"><a class="nav-link" href="/learn/final/index.html">首页</a></li>
          <li class="nav-item"><a class="nav-link" href="/learn/final/pricing.html">价格</a></li>
          <li class="nav-item"><a class="nav-link" href="/learn/final/dashboard.html">后台</a></li>
        </ul>
      </{D}>
    </{D}>
  </nav>"""

def bs_foot():
    return f'  <script src="{JS}"></script>\n</body>\n</html>\n'

# --- index ---
w("index.html", f"""<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Bootstrap & CSS 学习中心</title>
  <link href="{BS}" rel="stylesheet">
  <link href="/learn/css/custom.css" rel="stylesheet">
</head>
<body>
  <{D} class="learn-banner">泰安项目 · 前端学习 | <code>http://localhost:9005/taian/learn</code></{D}>
  <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <{D} class="container">
      <a class="navbar-brand" href="/learn/index.html">CloudNote 学习</a>
      <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navMain">
        <span class="navbar-toggler-icon"></span>
      </button>
      <{D} class="collapse navbar-collapse" id="navMain">
        <ul class="navbar-nav ms-auto">
          <li class="nav-item"><a class="nav-link" href="/taian/price">业务：价格页</a></li>
          <li class="nav-item"><a class="nav-link" href="/taian/updatePrice">业务：更新价格</a></li>
        </ul>
      </{D}>
    </{D}>
  </nav>
  <header class="bg-primary text-white py-5 text-center">
    <{D} class="container">
      <h1 class="display-5 fw-bold">7 天 Bootstrap + CSS 实战</h1>
      <p class="lead">每天一页，覆盖常用知识点</p>
    </{D}>
  </header>
  <main class="container py-5">
    <{D} class="row g-4">
      <{D} class="col-md-6 col-lg-4"><{D} class="card h-100 border-primary"><{D} class="card-header bg-primary text-white fw-bold">Day 1 · 纯 CSS</{D}><{D} class="card-body"><p class="small text-muted">Flex · 盒模型 · 选择器</p><a href="/learn/day1/practice.html" class="btn btn-primary btn-sm">开始练习</a></{D}></{D}></{D}>
      <{D} class="col-md-6 col-lg-4"><{D} class="card h-100"><{D} class="card-header fw-bold">Day 2 · 响应式</{D}><{D} class="card-body"><p class="small text-muted">@media · viewport</p><a href="/learn/day2/responsive.html" class="btn btn-outline-primary btn-sm">开始练习</a></{D}></{D}></{D}>
      <{D} class="col-md-6 col-lg-4"><{D} class="card h-100"><{D} class="card-header fw-bold">Day 3 · Bootstrap</{D}><{D} class="card-body"><p class="small text-muted">Navbar · Hero</p><a href="/learn/day3/index.html" class="btn btn-outline-primary btn-sm">开始练习</a></{D}></{D}></{D}>
      <{D} class="col-md-6 col-lg-4"><{D} class="card h-100"><{D} class="card-header fw-bold">Day 4 · 栅格 + Card</{D}><{D} class="card-body"><p class="small text-muted">row/col · card</p><a href="/learn/day4/features.html" class="btn btn-outline-primary btn-sm">开始练习</a></{D}></{D}></{D}>
      <{D} class="col-md-6 col-lg-4"><{D} class="card h-100"><{D} class="card-header fw-bold">Day 5 · 表单 + 价格</{D}><{D} class="card-body"><p class="small text-muted">form · badge</p><a href="/learn/day5/pricing.html" class="btn btn-outline-primary btn-sm">开始练习</a></{D}></{D}></{D}>
      <{D} class="col-md-6 col-lg-4"><{D} class="card h-100"><{D} class="card-header fw-bold">Day 6 · 后台布局</{D}><{D} class="card-body"><p class="small text-muted">table · sidebar</p><a href="/learn/day6/dashboard.html" class="btn btn-outline-primary btn-sm">开始练习</a></{D}></{D}></{D}>
    </{D}>
    <section class="mt-5 p-4 bg-light rounded">
      <h2 class="h4">Day 7 · 综合实战（最终目标）</h2>
      <p class="text-muted">三个页面互通导航，自行改品牌与配色。</p>
      <a href="/learn/final/index.html" class="btn btn-success btn-sm me-2">最终首页</a>
      <a href="/learn/final/pricing.html" class="btn btn-success btn-sm me-2">最终价格页</a>
      <a href="/learn/final/dashboard.html" class="btn btn-success btn-sm">最终后台</a>
    </section>
  </main>
  <footer class="text-center py-4 text-muted border-top"><small>业务页面不受影响</small></footer>
  <script src="{JS}"></script>
</body>
</html>
""")

# --- day1 ---
w("day1/practice.html", f"""<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Day 1 · 纯 CSS 基础</title>
  <link rel="stylesheet" href="../css/day1.css">
</head>
<body>
  <{D} class="exercise-tip"><strong>Day 1 任务：</strong> 阅读 day1.css；改品牌名；给 .card 加 hover 效果；F12 看盒模型。</{D}>
  <header class="site-header">
    <{D} class="logo">CloudNote</{D}>
    <nav class="site-nav">
      <a href="/learn/index.html">学习中心</a>
      <a href="#">首页</a>
      <a href="#">价格</a>
    </nav>
  </header>
  <main class="card-row">
    <article class="card"><h3>快速</h3><p>秒级同步，多设备即时更新。</p></article>
    <article class="card"><h3>安全</h3><p>端到端加密保护数据。</p></article>
    <article class="card"><h3>协作</h3><p>团队共享，权限可控。</p></article>
  </main>
  <footer class="site-footer">Day 1 · <a href="/learn/day2/responsive.html">下一步 Day 2</a></footer>
</body>
</html>
""")

# --- day2 ---
w("day2/responsive.html", f"""<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Day 2 · 响应式</title>
  <link rel="stylesheet" href="/learn/css/day2.css">
</head>
<body>
  <{D} class="exercise-tip"><strong>Day 2 任务：</strong> 打开 day2.css，取消 @media 注释；F12 切到 375px 看三列变一列。</{D}>
  <header class="site-header">
    <{D} class="logo">CloudNote</{D}>
    <nav class="site-nav">
      <a href="/learn/index.html">学习中心</a>
      <a href="/learn/day1/practice.html">Day 1</a>
      <a href="/learn/day3/index.html">Day 3</a>
    </nav>
  </header>
  <main class="card-row">
    <article class="card"><h3>手机</h3><p>窄屏单列显示。</p></article>
    <article class="card"><h3>平板</h3><p>768px 断点练习。</p></article>
    <article class="card"><h3>桌面</h3><p>宽屏三列并排。</p></article>
  </main>
  <footer class="site-footer">Day 2 · <a href="/learn/day3/index.html">下一步 Day 3</a></footer>
</body>
</html>
""")

# --- day3 ---
w("day3/index.html", f"""<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Day 3 · Bootstrap 入门</title>
  <link href="{LOCAL_BS}" rel="stylesheet">
  <link href="/learn/css/custom.css" rel="stylesheet">
</head>
<body>""" + f"""
  <{D} class="alert alert-info m-3 mb-0"><strong>Day 3 任务：</strong> 补全下方 TODO；理解 navbar、container、btn、py-5 等类名。</{D}>
""" + nav_links() + f"""
  <section class="bg-primary text-white py-5">
    <{D} class="container text-center">
      <h1 class="display-4 fw-bold">云端笔记，随时随地</h1>
      <p class="lead mt-3">团队协作 · 安全加密 · 实时同步</p>
      <!-- TODO: 添加一个大按钮 btn btn-light btn-lg -->
    </{D}>
  </section>
""" + f'  <script src="{LOCAL_JS}"></script>\n</body>\n</html>\n')

# --- day4 ---
w("day4/features.html", bs_head("Day 4 · 栅格与卡片") + f"""
  <{D} class="alert alert-info m-3 mb-0"><strong>Day 4 任务：</strong> 在下方 row 内添加 3 个 col-md-4 卡片（参考注释）。</{D}>
""" + nav_links() + f"""
  <section class="container py-5">
    <h2 class="text-center mb-4">核心功能</h2>
    <{D} class="row g-4">
      <!-- TODO: 复制下面结构 3 次，改标题和描述
      <{D} class="col-md-4">
        <{D} class="card h-100">
          <{D} class="card-body text-center">
            <h5 class="card-title">功能名</h5>
            <p class="card-text text-muted">描述文字</p>
          </{D}>
        </{D}>
      </{D}>
      -->
      <{D} class="col-12"><p class="text-muted text-center">请在此添加你的三张卡片</p></{D}>
    </{D}>
  </section>
""" + bs_foot())

# --- day5 ---
w("day5/pricing.html", bs_head("Day 5 · 价格页") + f"""
  <{D} class="alert alert-info m-3 mb-0"><strong>Day 5 任务：</strong> 完成三档价格卡片；中间档加 badge「推荐」。</{D}>
""" + nav_links() + f"""
  <section class="container py-5">
    <h2 class="text-center mb-5">选择你的方案</h2>
    <{D} class="row g-4 justify-content-center">
      <!-- TODO: 三列 col-md-4，每列一张 card，含价格、ul 功能列表、按钮 -->
      <{D} class="col-12 text-center text-muted">在此添加基础版 / 专业版 / 企业版</{D}>
    </{D}>
  </section>
  <section class="container pb-5">
    <h3 class="h5 mb-3">订阅更新（表单练习）</h3>
      <!-- TODO: form 含 email 输入 + 提交按钮 -->
    <form class="row g-2 max-w-400">
      <{D} class="col-md-8">
        <input type="email" class="form-control" placeholder="your@email.com">
      </{D}>
      <{D} class="col-md-4">
        <button type="submit" class="btn btn-primary w-100">订阅</button>
      </{D}>
    </form>
  </section>
""" + bs_foot())

# --- day6 ---
w("day6/dashboard.html", bs_head("Day 6 · 后台布局") + f"""
  <{D} class="learn-banner">Day 6 · 侧边栏 + 表格 · 自定义 CSS 见 custom.css</{D}>
  <{D} class="container-fluid">
    <{D} class="row">
      <aside class="col-md-2 d-none d-md-block sidebar p-0">
        <{D} class="brand">CloudNote Admin</{D}>
        <a href="#" class="active">概览</a>
        <a href="/learn/final/pricing.html">价格</a>
        <a href="/learn/index.html">学习中心</a>
      </aside>
      <main class="col-md-10 p-4">
        <h1 class="h3 mb-4">数据概览</h1>
        <{D} class="row g-3 mb-4">
          <{D} class="col-md-4"><{D} class="card"><{D} class="card-body"><p class="text-muted mb-1">今日访问</p><p class="stat-value">1,280</p></{D}></{D}></{D}>
          <{D} class="col-md-4"><{D} class="card"><{D} class="card-body"><p class="text-muted mb-1">注册用户</p><p class="stat-value">356</p></{D}></{D}></{D}>
          <{D} class="col-md-4"><{D} class="card"><{D} class="card-body"><p class="text-muted mb-1">收入</p><p class="stat-value">¥8,900</p></{D}></{D}></{D}>
        </{D}>
        <h2 class="h5">用户列表</h2>
        <table class="table table-striped table-hover">
          <thead><tr><th>ID</th><th>用户名</th><th>状态</th></tr></thead>
          <tbody>
            <tr><td>1</td><td>张三</td><td><span class="badge bg-success">正常</span></td></tr>
            <tr><td>2</td><td>李四</td><td><span class="badge bg-warning">待审</span></td></tr>
            <tr><td>3</td><td>王五</td><td><span class="badge bg-secondary">停用</span></td></tr>
          </tbody>
        </table>
        <p class="text-muted small">TODO: 再加 2 行数据；尝试改 sidebar 背景色</p>
      </main>
    </{D}>
  </{D}>
""" + bs_foot())

# --- final pages (Day 7 target - mostly complete) ---
w("final/index.html", bs_head("CloudNote · 首页") + nav_links() + f"""
  <section class="bg-primary text-white py-5 text-center">
    <{D} class="container">
      <h1 class="display-4 fw-bold">云端笔记</h1>
      <p class="lead">安全 · 快速 · 团队协作</p>
      <a href="/learn/final/pricing.html" class="btn btn-light btn-lg mt-3">查看价格</a>
    </{D}>
  </section>
  <section class="container py-5">
    <{D} class="row g-4">
      <{D} class="col-md-4"><{D} class="card h-100"><{D} class="card-body text-center"><h5>实时同步</h5><p class="text-muted">多设备即时更新</p></{D}></{D}></{D}>
      <{D} class="col-md-4"><{D} class="card h-100"><{D} class="card-body text-center"><h5>安全加密</h5><p class="text-muted">数据全程保护</p></{D}></{D}></{D}>
      <{D} class="col-md-4"><{D} class="card h-100"><{D} class="card-body text-center"><h5>团队协作</h5><p class="text-muted">权限灵活配置</p></{D}></{D}></{D}>
    </{D}>
  </section>
  <section class="container pb-5">
    <h2 class="h4 mb-3">常见问题</h2>
    <{D} class="accordion" id="faq">
      <{D} class="accordion-item">
        <h2 class="accordion-header"><button class="accordion-button" type="button" data-bs-toggle="collapse" data-bs-target="#q1">如何开始？</button></h2>
        <{D} id="q1" class="accordion-collapse collapse show" data-bs-parent="#faq"><{D} class="accordion-body">注册账号即可免费使用基础版。</{D}></{D}>
      </{D}>
      <{D} class="accordion-item">
        <h2 class="accordion-header"><button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#q2">支持哪些设备？</button></h2>
        <{D} id="q2" class="accordion-collapse collapse" data-bs-parent="#faq"><{D} class="accordion-body">Web、iOS、Android 全平台支持。</{D}></{D}>
      </{D}>
    </{D}>
  </section>
  <footer class="text-center py-4 text-muted border-top"><small>Day 7 参考首页 · 可自行修改</small></footer>
""" + bs_foot())

w("final/pricing.html", bs_head("CloudNote · 价格") + nav_links() + f"""
  <section class="container py-5">
    <h1 class="text-center mb-5">价格方案</h1>
    <{D} class="row g-4">
      <{D} class="col-md-4"><{D} class="card h-100"><{D} class="card-body text-center"><h5>基础版</h5><p class="display-6">¥0</p><ul class="list-unstyled"><li>5GB 空间</li><li>基础同步</li></ul><a href="#" class="btn btn-outline-primary">免费开始</a></{D}></{D}></{D}>
      <{D} class="col-md-4"><{D} class="card h-100 pricing-card-featured shadow"><{D} class="card-body text-center"><span class="badge bg-primary mb-2">推荐</span><h5>专业版</h5><p class="display-6">¥29</p><ul class="list-unstyled"><li>100GB 空间</li><li>团队协作</li></ul><a href="#" class="btn btn-primary">立即订阅</a></{D}></{D}></{D}>
      <{D} class="col-md-4"><{D} class="card h-100"><{D} class="card-body text-center"><h5>企业版</h5><p class="display-6">¥99</p><ul class="list-unstyled"><li>无限空间</li><li>专属客服</li></ul><a href="#" class="btn btn-outline-primary">联系销售</a></{D}></{D}></{D}>
    </{D}>
  </section>
""" + bs_foot())

w("final/dashboard.html", bs_head("CloudNote · 后台") + f"""
  <{D} class="container-fluid">
    <{D} class="row">
      <aside class="col-md-2 d-none d-md-block sidebar p-0">
        <{D} class="brand">Admin</{D}>
        <a href="/learn/final/index.html">首页</a>
        <a href="/learn/final/pricing.html">价格</a>
        <a href="/learn/final/dashboard.html" class="active">后台</a>
      </aside>
      <main class="col-md-10 p-4">
        <h1 class="h3">控制台</h1>
        <{D} class="row g-3 my-3">
          <{D} class="col-md-4"><{D} class="card"><{D} class="card-body"><p class="text-muted">订单</p><p class="stat-value">42</p></{D}></{D}></{D}>
          <{D} class="col-md-4"><{D} class="card"><{D} class="card-body"><p class="text-muted">用户</p><p class="stat-value">128</p></{D}></{D}></{D}>
          <{D} class="col-md-4"><{D} class="card"><{D} class="card-body"><p class="text-muted">收入</p><p class="stat-value">¥5.2k</p></{D}></{D}></{D}>
        </{D}>
        <table class="table table-hover"><thead><tr><th>订单号</th><th>金额</th><th>状态</th></tr></thead>
        <tbody><tr><td>#1001</td><td>¥29</td><td><span class="badge bg-success">已付</span></td></tr>
        <tr><td>#1002</td><td>¥99</td><td><span class="badge bg-warning">待付</span></td></tr></tbody></table>
      </main>
    </{D}>
  </{D}>
""" + bs_foot())

print("All learning pages generated.")
