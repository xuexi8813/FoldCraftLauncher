<div align="center">
    <img width="96" src="/FCL/src/mio/res/mipmap-xxxhdpi/ic_launcher.png"></img>
</div>

<h1 align="center">FCL-澪（Fold Craft Launcher · 澪风格复刻版）</h1>

<div align="center">

[![Back to FCL](https://img.shields.io/badge/%E2%86%90%20%E8%BF%94%E5%9B%9E%E5%8E%9F%E7%89%88%20FCL%20README-blue)](./README.md)

</div>

> 🌸 **本分支（`fcl-mio`）是在 [Fold Craft Launcher](./README.md) 基础上，对 [澪-Ultimate 启动器]（ShirosakiMio 作品）界面风格的复刻版本。**
> FCL 的全部功能完整保留；澪原版没有的界面（下载页、控制器、联机等）一律用澪的设计语言绘制。

---

## ✨ 复刻内容

| 澪元素 | fcl-澪 实现 |
|---|---|
| 粉色主题（`#F97297` / 深 `#FF5180`），另有蓝/绿/紫/红可选 | 默认主题色改为澪粉，FCL 原有"自定义主题色"功能保留，用户可切任意色 |
| 顶部工具栏 + 左侧分类侧栏（MC 像素图标 + 分组小标题） | `MioMenuView`（图标+文字条目），侧栏分组：启动器 / 游戏 / 设置 |
| 半透明白面板浮于场景背景之上 | `ui_bg_color`/`bg_container_white`/`bg_game_menu` 等资源统一覆盖 |
| 右下角"开始游戏"色块 | 右下角启动块：账户行 + 版本卡片 + 主题色实心启动按钮 |
| 公告面板 | FCL 公告容器套用澪的面板风格 |
| 场景照片背景 | 内置默认背景改为澪同款场景（仅 flavor 默认值，可在设置中更换，含亮/暗两套） |
| 启动图标 | 重制的粉底白"澪"字图标（未复制澪美术资产） |

**功能零删减**：版本管理、下载、控制器布局、多人联机、皮肤、视频背景、渲染器插件、Vulkan 检测等全部 FCL 功能均可用。

## 🔧 构建

```bash
# fcl-澪（包名 com.tungsten.fcl.mio，可与原版共存）
gradlew :FCL:assembleMioDebug -Darch=arm64     # 或 arm / x86 / x86_64 / all

# 原版 FCL（同一分支随时可构建）
gradlew :FCL:assembleStandardDebug -Darch=arm64
```

产物命名：原版 `FCL-release-<版本>-<abi>.apk`，澪版 `FCL-mio-release-<版本>-<abi>.apk`。

## 🗂 工程结构（低冲突同步设计）

```
FCL/src/main/   ← 原版 FCL（除 2 处 [mio] 挂钩外零改动）
FCL/src/mio/    ← 澪定制层（flavor 资源覆盖，全部新增文件）
├── AndroidManifest.xml      仅替换 Application 类
├── java/.../MioApp.kt       启动时播种澪默认粉主题
├── java/.../MioMenuView.java 澪侧栏条目（图标+文字）
└── res/                     colors/drawables/layout/mipmap 覆盖
```

## 🔄 上游同步指南

澪版所有 UI 都在 `src/mio` 源集（资源按名称覆盖 main），**上游对 main 资源的改动不会产生 git 冲突**。同步步骤：

```bash
git fetch upstream
git checkout fcl-mio
git merge upstream/main
```

合并后需要人工核对的两类点：

1. **共享代码中的 `[mio]` 挂钩**（全局仅以下几处，冲突时保留两侧逻辑）：
   - `FCL/build.gradle.kts` — flavor 定义、`file_browser_provider` 动态化、APK 命名
   - `FCL/src/main/java/com/tungsten/fcl/activity/MainActivity.kt` — 2 处 `// [mio]` 分支（侧栏底色、启动按钮底色）
   - `FCL/src/main/java/com/tungsten/fcllibrary/component/view/FCLDynamicIsland.java` — 1 处 `// [mio]` 分支（澪顶栏下动态岛隐藏药丸、白色粗体标题）
   - `README.md` — 头部跳转链接
2. **上游布局变更的移植**：上游若修改了 `activity_main.xml` / `ui_*.xml`（新增视图 ID 等），需把对应变更手动移植到 `src/mio/res/layout/` 的同名覆盖文件（否则 mio 变体仍是旧结构）。

## 📐 设计参考

澪设计规范提取（调色板/布局骨架/控件语言）：仓库外分析目录 `_mio_analysis/design_spec.md`。

## 📜 版权说明

- 本项目遵循 FCL 原项目的 **GPL-3.0** 协议。
- "澪-Ultimate" 为 ShirosakiMio 的作品，本分支复刻其界面风格（布局结构/配色/交互语言）；应用启动图标为从澪 APK 中提取的角色立绘（应用所有者要求），仅供个人复刻研究使用，澪相关美术的权利归其作者所有；内置背景图为 Minecraft 场景截图、侧栏像素图标为 Minecraft 游戏素材。
- 本分支为个人学习复刻用途，澪相关商标与权利归其作者所有。
