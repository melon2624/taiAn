const { useMemo, useState } = React;

const initialUsers = [
  { id: 1001, name: "张三", role: "管理员", department: "运营部", status: "active" },
  { id: 1002, name: "李四", role: "编辑", department: "内容部", status: "pending" },
  { id: 1003, name: "王五", role: "访客", department: "财务部", status: "disabled" },
  { id: 1004, name: "赵六", role: "开发", department: "技术部", status: "active" }
];

const statusText = {
  active: "正常",
  pending: "待审核",
  disabled: "停用"
};

function statusClass(status) {
  return `status-pill status-${status}`;
}

function Sidebar() {
  return (
    <aside className="col-md-2 react-sidebar">
      <div className="brand">React Admin</div>
      <a className="active" href="/learn/react/admin.html">用户管理</a>
      <a href="/learn/react/index.html">学习路线</a>
      <a href="/learn/index.html">学习中心</a>
    </aside>
  );
}

function MetricCard({ label, value }) {
  return (
    <div className="col-md-4">
      <div className="metric-card">
        <div className="text-muted">{label}</div>
        <div className="metric-value">{value}</div>
      </div>
    </div>
  );
}

function UserForm({ onCreate }) {
  const [form, setForm] = useState({
    name: "",
    role: "编辑",
    department: "运营部",
    status: "active"
  });

  function updateField(field, value) {
    setForm({ ...form, [field]: value });
  }

  function handleSubmit(event) {
    event.preventDefault();

    if (!form.name.trim()) {
      alert("请输入用户名");
      return;
    }

    onCreate({
      id: Date.now(),
      name: form.name.trim(),
      role: form.role,
      department: form.department,
      status: form.status
    });

    setForm({ ...form, name: "" });
  }

  return (
    <form className="card mb-4" onSubmit={handleSubmit}>
      <div className="card-header fw-bold">新增用户</div>
      <div className="card-body">
        <div className="row g-3">
          <div className="col-md-3">
            <label className="form-label">用户名</label>
            <input
              className="form-control"
              value={form.name}
              onChange={(event) => updateField("name", event.target.value)}
              placeholder="例如：钱七"
            />
          </div>
          <div className="col-md-3">
            <label className="form-label">角色</label>
            <select className="form-select" value={form.role} onChange={(event) => updateField("role", event.target.value)}>
              <option>管理员</option>
              <option>编辑</option>
              <option>开发</option>
              <option>访客</option>
            </select>
          </div>
          <div className="col-md-3">
            <label className="form-label">部门</label>
            <select className="form-select" value={form.department} onChange={(event) => updateField("department", event.target.value)}>
              <option>运营部</option>
              <option>内容部</option>
              <option>财务部</option>
              <option>技术部</option>
            </select>
          </div>
          <div className="col-md-2">
            <label className="form-label">状态</label>
            <select className="form-select" value={form.status} onChange={(event) => updateField("status", event.target.value)}>
              <option value="active">正常</option>
              <option value="pending">待审核</option>
              <option value="disabled">停用</option>
            </select>
          </div>
          <div className="col-md-1 d-flex align-items-end">
            <button className="btn btn-primary w-100" type="submit">新增</button>
          </div>
        </div>
      </div>
    </form>
  );
}

function UserTable({ users, onRemove, onToggleStatus }) {
  if (users.length === 0) {
    return (
      <div className="card">
        <div className="card-body text-center text-muted py-5">没有找到匹配的用户</div>
      </div>
    );
  }

  return (
    <div className="card">
      <div className="table-responsive">
        <table className="table table-hover align-middle mb-0">
          <thead className="table-light">
            <tr>
              <th>ID</th>
              <th>用户名</th>
              <th>角色</th>
              <th>部门</th>
              <th>状态</th>
              <th className="text-end">操作</th>
            </tr>
          </thead>
          <tbody>
            {users.map((user) => (
              <tr key={user.id}>
                <td>{user.id}</td>
                <td className="fw-semibold">{user.name}</td>
                <td>{user.role}</td>
                <td>{user.department}</td>
                <td><span className={statusClass(user.status)}>{statusText[user.status]}</span></td>
                <td className="text-end">
                  <button className="btn btn-outline-secondary btn-sm me-2" onClick={() => onToggleStatus(user.id)}>切换状态</button>
                  <button className="btn btn-outline-danger btn-sm" onClick={() => onRemove(user.id)}>删除</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}

function AdminApp() {
  const [users, setUsers] = useState(initialUsers);
  const [keyword, setKeyword] = useState("");
  const [status, setStatus] = useState("all");

  const filteredUsers = useMemo(() => {
    return users.filter((user) => {
      const hitKeyword =
        user.name.includes(keyword) ||
        user.role.includes(keyword) ||
        user.department.includes(keyword);
      const hitStatus = status === "all" || user.status === status;
      return hitKeyword && hitStatus;
    });
  }, [users, keyword, status]);

  const activeCount = users.filter((user) => user.status === "active").length;
  const pendingCount = users.filter((user) => user.status === "pending").length;

  function createUser(user) {
    setUsers([user, ...users]);
  }

  function removeUser(id) {
    setUsers(users.filter((user) => user.id !== id));
  }

  function toggleStatus(id) {
    setUsers(
      users.map((user) => {
        if (user.id !== id) {
          return user;
        }

        const nextStatus = user.status === "active" ? "disabled" : "active";
        return { ...user, status: nextStatus };
      })
    );
  }

  return (
    <div className="container-fluid react-shell admin-page">
      <div className="row">
        <Sidebar />

        <main className="col-md-10 p-4">
          <div className="d-flex justify-content-between align-items-center mb-4">
            <div>
              <h1 className="h3 mb-1">用户管理</h1>
              <p className="text-muted mb-0">练习组件、状态、列表、表单、筛选和事件处理。</p>
            </div>
            <a className="btn btn-outline-primary" href="/learn/react/index.html">返回路线</a>
          </div>

          <div className="row g-3 mb-4">
            <MetricCard label="总用户" value={users.length} />
            <MetricCard label="正常用户" value={activeCount} />
            <MetricCard label="待审核" value={pendingCount} />
          </div>

          <UserForm onCreate={createUser} />

          <div className="card mb-3">
            <div className="card-body toolbar">
              <input
                className="form-control"
                style={{ maxWidth: 320 }}
                value={keyword}
                onChange={(event) => setKeyword(event.target.value)}
                placeholder="搜索用户名、角色、部门"
              />
              <select className="form-select" style={{ maxWidth: 180 }} value={status} onChange={(event) => setStatus(event.target.value)}>
                <option value="all">全部状态</option>
                <option value="active">正常</option>
                <option value="pending">待审核</option>
                <option value="disabled">停用</option>
              </select>
              <span className="text-muted">当前显示 {filteredUsers.length} 条</span>
            </div>
          </div>

          <UserTable users={filteredUsers} onRemove={removeUser} onToggleStatus={toggleStatus} />
        </main>
      </div>
    </div>
  );
}

ReactDOM.createRoot(document.getElementById("root")).render(<AdminApp />);
