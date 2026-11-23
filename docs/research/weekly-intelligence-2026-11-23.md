# UTV 综合技术情报周报 — 2026-11-23

**核查日期：** 2026-11-23  
**范围：** UTV 全模块、Cloud IPTV Hub、IPTV 源与客户端、tvOS、NAS/媒体服务器、Super Player 可靠性、Music Center、Folia/Aerial、代理订阅转换、Movie Lite/TVOS。  
**状态原则：** Study / Trial / Adopt / Hold / Reject 与“架构批准 / 已部署 / 已实测 / 生产验收”分别记录，不能互相替代。

## 0. 主线结论

1. **Cloud IPTV Hub 可按“云端订阅发布 + 设备身份 + UTV 客户端配对”实现自动同步，但还没有端到端实现。** Cloudflare 适合托管版本化 M3U/EPG 和轻量授权 API，不负责直播源质量、视频转码或播放稳定性。
2. **本周 IPTV 公开播放列表已成功读取并解析，但没有完成源 URL HTTP 探测、HLS 分片检查或真实播放。** 用户确认可用的中国大陆 IPTV V1.0 基线保持不变。
3. **值得重点试验的客户端是 Marco Movie Android TV 0.6.2 和 Jellyfin Android TV 0.20.0。** 两者均有官方发布记录，但没有 Toshiba R700 实机验收，均保持 Study。
4. **NAS 生态有实质版本变化。** TrueNAS 25.10.8 包含重要安全修复；OpenMediaVault 上游源码 changelog 已推进到 8.7.2-1，但本次未确认对应正式安装镜像。
5. **音乐生态出现重要的数据安全案例。** AlgerMusicPlayer 新增 WebDAV 同步，但远端备份可包含明文登录 Cookie 和插件密钥；UTOS 不应照搬这种默认数据模型。
6. **代理订阅转换主线已更新至 SubConverter-Extended 1.11.8。** 解析能力变化有源码/发布证据，尚无统一脱敏配置的性能基准或真实 Mihomo 客户端兼容测试。
7. **Super Player 没有新的 R700 故障日志和可重复测试数据。** 不能把“本周没有新日志”解释为“没有故障”，也不能宣布已找到或修复 MyTV 长时间冻结根因。

## 1. 证据等级

- **A：** 官方仓库、正式发布、官方文档或可重复读取的上游资料。
- **B：** 本次实际执行的读取、解析或测试；必须注明测试对象和边界。
- **C：** 基于 A/B 的工程推断。
- **D：** 设计目标。
- **待验证：** 资料缺失、来源冲突、没有真实设备或端到端证据。

本报告的 M3U 计数属于 **B（文件读取与静态解析）**；播放器、NAS、路由器和 Cloudflare 部署兼容性均未完成 B 级实测。

## 2. IPTV / Cloud IPTV Hub 主线

### 2.1 上游版本与候选

| 项目 | 本周核查 | 结论 / UTV 状态 |
|---|---|---|
| Guovin IPTV-API | v3.1.0，2026-10-12 | **Study**。最低 Python 3.14；桌面更新校验、配置/输出目录迁移、GitHub Pages/Release 订阅分发、频道别名扩展、EPG/直播采样资源限制和依赖安全更新。open_subscribe_epg 默认关闭，需要显式开启。升级必须在隔离环境核对配置迁移。 |
| IPTV Checker | v5.0.2 标签存在 | **Study**。官方页面中 v5.0.2 与 v5.0.1 指向同一提交 SHA，且没有可区分的实质变更说明；不能仅凭版本号判断有新功能或值得升级。 |
| IPTV Checker Player | 0.1.0 源码候选；最后可见提交 2026-09-06 | **Hold**。Tauri 2 多端客户端依赖独立的 iptv-checker-rs 服务；客户端仓库未发现 LICENSE，不能作为可复用生产依赖。 |
| iptv-checker-rs | Rust 服务端候选 | **Study**。仓库声明 MIT；其代理/FFmpeg 中继路径与普通 M3U 检测器职责不同，不能与 zhimin-dev/iptv-checker 混为同一产品。 |
| TVGO / VsTV | 名称检索命中多个不相关或分叉仓库 | **待验证**。尚未确认用户所指项目的唯一官方仓库，不猜测或替用户选定上游。 |
| Smart IPTV Server、IPTV-4GTV、4GTV-v1、Bee-v1/Bee-v2 | 本周未取得新的源码、许可证、镜像签名或安全证据 | **Hold / Study**。保持独立记录，不合并、不运行不明镜像。 |

Guovin v3.1.0 的升级关注点：Python 3.14 环境、配置默认值、Docker 代理变量、SQLite WAL、HLS 临时目录、EPG 自动发现开关和依赖安全更新。未在 Windows 11 / Docker Desktop / WSL2 实测。

### 2.2 中国大陆 IPTV V1.0 源基线

保留用户已确认可用的 V1.0 优先级，不覆盖、不替换。此次从 GitHub 读取并解析了以下公开文件：

| 播放列表 | EXTINF 条目 | 不同显示名称 | HTTP URL | HTTPS URL | 说明 |
|---|---:|---:|---:|---:|---|
| best-fan / cn_cctv_status.m3u8 | 115 | 35 | 107 | 8 | 同一频道名存在多条候选线路；URL 字符串未重复 |
| best-fan / cn_province_status.m3u8 | 138 | 34 | 128 | 10 | 同一频道名存在多条候选线路；URL 字符串未重复 |
| best-fan / cn_all_status.m3u8 | 254 | 70 | 236 | 18 | 同一频道名存在多条候选线路；URL 字符串未重复 |
| hujingguang / cnTV_AutoUpdate.m3u8 | 49 | 频道名解析口径不统一 | 13 | 36 | 48 个唯一 URL，发现 1 个完全重复 URL |

- best-fan 相关文件最近可见提交：2026-11-22。
- hujingguang/ChinaIPTV 最近可见更新：2026-11-23。
- best-fan 文件中 HTTP 明文 URL 占多数；这只是列表内容统计，不代表这些地址一定不可用，但存在传输被观察或篡改的风险。
- response-time 等 M3U 属性属于上游提供的元数据，不是本次测量结果。
- 重复频道名不等于重复流：可能是同频道的多条候选线路。不能把它们直接删除。

官方文件：
- [best-fan CCTV](https://github.com/best-fan/iptv-sources/blob/main/cn_cctv_status.m3u8)
- [best-fan Province](https://github.com/best-fan/iptv-sources/blob/main/cn_province_status.m3u8)
- [best-fan All](https://github.com/best-fan/iptv-sources/blob/main/cn_all_status.m3u8)
- [hujingguang ChinaIPTV](https://github.com/hujingguang/ChinaIPTV/blob/main/cnTV_AutoUpdate.m3u8)

**本次实际完成：** 获取文件、解析 EXTINF、统计显示名称和 URL 字符串、统计 HTTP/HTTPS。  
**本次未完成：** 对每条 URL 发起 HTTP 请求、测量响应时间、跟随重定向、检查 HLS master/media playlist、下载分片、解码首帧或持续播放。故本周不发布成功率、延迟排名或“低延迟线路”结论。

### 2.3 Cloudflare 与 GitHub 部署边界

按 2026-11-23 可见的官方额度：

| 服务 | Free 基线 | 对 UTV 的含义 |
|---|---|---|
| Pages | 500 次构建/月；单项目 20,000 文件；单文件 25 MiB | 管理 UI 与小型静态文件可用；较大 EPG/资源文件放 R2。 |
| Workers | 100,000 请求/日；每次调用 10 ms CPU | 只承担轻量鉴权、设备 API、版本指针；不把大规模抓源、FFmpeg、HLS 探测放在免费 Worker 请求路径。 |
| Workers Paid | 基础订阅 US$5/月；包含 10M 请求/月和 30M CPU ms/月 | 是否需要付费取决于实际设备数、刷新频率和 API 流量；目前没有流量模型，不能估算账单。 |
| KV | 100,000 读取/日、1,000 写入/日、1 GB；单值上限 25 MiB | 高读缓存适合；最终一致性可能跨地区延迟约 60 秒，不适合作为一次性配对码消费或强一致状态源。 |
| R2 | 10 GB-month、1M Class A、10M Class B/月；公网出站流量免费 | 适合版本化 M3U/EPG 与静态产物；仍有存储和操作额度，免费额度只适用于 Standard。 |
| D1 | 5M 行读取/日、100,000 行写入/日、总计 5 GB | 可作为设备、订阅版本和审计元数据的候选权威数据库；需设计索引、保留周期和配对码原子消费。 |

建议的边界：
- GitHub：源码、配置模板、版本、Actions 工作流和变更审计。
- GitHub Actions 或受控自托管 Worker：定时抓源、解析、去重、检测、EPG 匹配和生成版本化产物。
- R2：保存不可变版本的 M3U/TXT/JSON/XMLTV；发布成功后再更新当前版本指针。
- D1：设备绑定、授权、订阅版本、撤销状态和审计元数据。
- KV：只做可过期缓存，不保存唯一权威授权状态。
- Workers：鉴权、配对 API、读取版本清单和受控下载入口。
- Pages：管理界面，不承担视频转码。
- 不把 Cloudflare 静态托管当视频代理、直播源修复或转码服务。

**免手动配置边界：** UTV 自有客户端可通过二维码/一次性配对码绑定设备，再从受控 API 获取订阅和配置；VLC、Kodi、TiviMate 等第三方播放器必须逐个确认是否支持远程订阅刷新、受控配置导入或可验证同步接口。Cloudflare 无法替不支持这些接口的播放器自动配置。

### 2.4 端到端成熟度

| 链路阶段 | 本周证据 |
|---|---|
| 公开源读取、M3U 静态解析 | B：已完成文件读取与计数 |
| 语义去重、频道数据库 | D：设计目标，未实现 |
| HTTP / HLS 健康检测 | 待验证 |
| EPG 自动匹配 | 待验证 |
| 订阅生成与版本回滚 | 待验证 |
| Cloudflare 发布与鉴权 | 待验证，未部署 |
| 设备绑定与 UTV 客户端同步 | 待验证，未实现 |
| Kodi / VLC / TiviMate 等第三方读取 | 待验证 |
| 真实播放、故障降级和备用切换 | 待验证 |

## 3. 代理订阅转换 / Clash / Mihomo

- [SubConverter-Extended v1.11.8](https://github.com/Aethersailor/SubConverter-Extended/releases/tag/v1.11.8) 于 2026-11-21 发布，GPL-3.0；本周最新正式发布基线。解析器对 VLESS Reality 隐式 SNI 等场景有更新。
- [tindy2013/subconverter](https://github.com/tindy2013/subconverter)：本次未发现同周新正式版本；最近可见提交仍早于本周。
- [lonelam/subconverter-rs](https://github.com/lonelam/subconverter-rs)：最近可见提交 2026-08-29；没有同环境基准。
- [Mihomo v1.19.34](https://github.com/MetaCubeX/mihomo/releases/tag/v1.19.34) 于 2026-11-11 发布，包含 Shadowsocks 多用户监听、Snell v6 和多项 TUN/TFO/SOCKS 修复。
- [OpenWrt 25.12.6](https://github.com/openwrt/openwrt/releases/tag/v25.12.6) 于 2026-10-18 发布，修复 uhttpd、Mbed TLS、hostapd/wpa_supplicant、rpcd 和 LuCI 等安全问题。是否升级必须按实际路由器 target、配置备份和恢复方式核对。
- OpenClash 0.48.002 仍是已核实版本基线；社区报告显示 Smart core 与上游 Mihomo 的部分协议能力可能不同，尤其不能因为 Mihomo 支持 Snell v6 就推断 OpenClash Smart core 已支持。

**本周未做：** 没有导入真实订阅、没有运行公共在线转换服务、没有在客户端加载配置、没有做性能基准。  
**下一轮基准要求：** 同一份脱敏订阅、同节点数、同模板/规则、同目标客户端和运行环境；记录冷/热转换时间、CPU/RAM、输出字节数、节点保留率、解析错误率及客户端加载结果。转换耗时不能替代节点延迟或吞吐测试。

## 4. NAS / 家庭媒体服务器 / Cloud Drive

### 4.1 NAS 平台

- **TrueNAS 25.10.8**：官方 2026-11-05 版本说明，更新 Linux 6.12 LTS 并修复 1,600 余项上游 CVE，同时修复数据集口令在内部任务参数中明文暴露的问题。属于重要安全更新，但没有用户 NAS 型号和恢复路径，不执行升级。
- **OpenMediaVault**：上游仓库 2026-11-21 的 Debian changelog 已推进至 8.7.2-1；本次没有确认对应正式 ISO/安装镜像或发行公告。不能把源码 changelog 直接写成用户可安装的稳定包版本。
- **OpenList**：v4.2.9 仍作为此前稳定版基线；11-17 主分支移除 Lite 构建变体/镜像，修复 Quark Open 内容哈希判断；11-13 的提交涉及令牌轮换持久化和 SQLite WAL。主分支变化不等于已发布镜像。
- **rclone 1.76.0**：稳定版基线；11-22 主分支增加本地多线程上传的 1 MiB 缓冲读取优化。上游提交附有 Windows/Linux 环境基准，但不是 UTV 或用户 NAS 实测，也不是新的稳定版发布。

### 4.2 Jellyfin / 客户端版本边界

- **Jellyfin Server 12.0** 于 2026-09-08 发布；**Jellyfin Web 12.4** 于 2026-10-26 发布。两者是不同组件版本，不能把 Web 12.4 写成 Server 12.4。
- Server 12.0 升级要求完整备份；建议迁移前移除第三方插件，升级后执行完整媒体库扫描；旧 /emby/*、/mediabrowser/* 路由和旧授权行为发生变化。没有完整备份和插件兼容测试，不得升级生产服务器。
- **Jellyfin Android TV 0.20.0** 于 2026-11-10 发布，最低 Android 6 / API 23，使用 Jellyfin 10.11 SDK，改进设置、视频播放、字幕和外部播放器选择。API 最低要求不代表 R700 已兼容，且 Server 12.0 的端到端兼容仍需验证。

### 4.3 数据安全

- RAID 不是备份；快照不是独立备份。
- 生产 NAS 的 Adopt 门槛仍包括权限审计、离线/不可变副本、校验、磁盘更换、断电恢复、勒索软件恢复演练、RPO/RTO 和异地恢复。
- 不在未完成恢复演练前安装或升级生产 NAS；不启用不必要的 privileged、Docker socket 或公网 SMB/NFS/管理面板。

## 5. Apple TV / tvOS

- [Swiftfin 1.7.1](https://github.com/jellyfin/Swiftfin/releases/tag/1.7.1) 于 2026-11-16 发布，是本周核实的最新版本。1.7.0 已引入 Live TV Guide、mpv、SwiftVLC 和 tvOS 标签栏设置；1.7.1 增加过滤器持久化/隐藏空过滤器、令牌撤销后重新登录提示及若干 tvOS 修复。
- Swiftfin 是 Jellyfin 的原生 iOS/tvOS 客户端；TestFlight 构建与 App Store 正式版本必须分开记录，邀请、构建有效期、tvOS 版本和地区 storefront 都要核实。
- Infuse、Plex、Emby、Filebar/Fileball、IIVA、Leplayer、AirWeb、Wobbly TV、Dawnboard、Speedtest 等候选本周没有足够新证据支持状态升级；Filebar/Fileball 命名与功能冲突继续标待验证。
- CleanYourTV、Aerial TV Launcher、FLauncher 是 Android/Google TV 生态，不能归类为 tvOS 应用。
- 本周没有操作 Apple TV、安装 TestFlight 或侧载任何 tvOS 项目。

## 6. UTV Movie Lite / TVOS / Android TV 影视客户端

### 6.1 Marco Movie

[Marco Movie Android TV 0.6.2](https://github.com/Pilipala9919/MarcoMovie/releases/tag/android-tv-v0.6.2) 于 2026-11-23 发布，最低 Android 6 / API 23。上游发布说明包含：
- 修复播放器释放后 ExoPlayer 状态查询引发的 NullPointerException。
- 修复 libmpv 原生 SIGABRT，并针对 32 位 armeabi-v7a 控制解复用缓存和高开销 HDR 峰值计算。
- MPV 初始化异常或 DRM 加密流回退到 Media3。
- 订阅源每日刷新、多源导入与自动分组、网盘中心和配置中心兼容。

上游声称 35 组 JVM 回归套件通过，并完成 API 23 字节码、正式签名和 zipalign 检查；同一发布说明明确标注没有完成实体电视安装和真实长时间播放验收。因此 **Study，不是 R700 兼容结论**。其内容策略声明为工具不提供影视资源，用户需自行合法导入配置；UTV 仍需审查每个 Provider 的来源、条款和凭据处理。

### 6.2 TVBox 生态

- [hgtuj5688/tvbox-desktop](https://github.com/hgtuj5688/tvbox-desktop)：0.1.0、MIT，最后可见提交 2026-09-27；Windows Electron 客户端。没有发现可验证的代码签名；README 中的频道数量和内置源宣传不作为实测或版权授权证据。Study，仅研究 UI、解析和目录结构。
- [CatVodTVOfficial/TVBoxOSC](https://github.com/CatVodTVOfficial/TVBoxOSC) 当前路径未能读取，多个 fork/构建聚合器存在；不能把任一 fork 说成官方上游。Hold。
- 海报/元数据存在不代表视频可播放；目录收录不代表内容有播放授权。
- TVOS 继续作为 UTV Media Center 内的自有影视元数据/资源目录子系统；Movie Lite 负责轻量发现与浏览；Super Player 负责播放内核；Provider 负责合法来源适配。没有新增代码或部署。

## 7. UTOS Music Center / Folia / Aerial

### 7.1 音乐服务与客户端

- **AlgerMusicPlayer 5.1.0**：11-20 主分支提交新增 WebDAV 同步，分类覆盖设置、音源插件、播放记录、收藏和登录状态；支持分类冲突处理、历史合并和定时同步。WebDAV 密码本地使用 Electron safeStorage 加密，但远端 backup.json 中的登录 Cookie 和插件变量密钥会以明文上传；登录同步默认关闭并要求确认。UTOS 应复用分类同步/冲突模型，但必须做客户端加密封装、默认排除凭据和密钥，并记录审计。
- **Navidrome 0.64.2**：11-23 主分支提交增加将 Internet Radio 站点暴露为 Jellyfin Live TV Radio channel 的适配。这是未发布的源码变化，不等于稳定版功能。UTV 可研究其跨 Music/Live TV 适配边界，但音乐广播与 IPTV 视频频道仍保持独立数据模型。
- **Feishin 1.17.0**：本周有依赖/搜索修复提交；无新正式版本证据。
- **Finamp 1.0.2**：本周有维护提交；没有 Android TV D-pad/遥控器实机证据。

### 7.2 Folia / Aerial

- **Folia / Realeco 0.7.29** 于 2026-11-16 发布，包含 Lattice 电台墙、歌词视觉主题、动画性能检测、Windows 分析性能优化和大规模本地音乐导入优化。AGPL-3.0。
- Folium 2 音源 Provider 接口仍需区分 alpha/预览代码与正式发布包；11-22 源码提交的接口变化尚未形成新的正式发行版。Android TV 移植仓库最近可见更新早于本周，未证明 R700 兼容。
- **Aerial TV Launcher** 上游 11-23 提交更新源码版本标记至 2.17（versionCode 161），Lite 1.40（versionCode 42）；MIT。属于 Android TV Launcher，不是 aerialtv.app 商业产品，也不是 tvOS 应用。没有正式安装包签名和 R700 实测证据。
- 11 月音乐完整盘点已在 2026-11-16 完成；本周只记录上述实质变化，不重复生成月度报告。

## 8. Super Player 可靠性与故障复盘

- 本周没有收到 R700 Logcat、MyTV/VLC 播放日志、音视频时间戳、CPU/RAM/温控或可重复复现记录。
- 已知 MyTV 长时间播放冻结仍没有已证实根因；不能把网络、解码器、Surface、状态机或温控中的任何一项直接写成根因。
- Marco Movie 0.6.2 的 ExoPlayer release-state 空指针和 libmpv SIGABRT 修复可加入回归测试设计参考，但属于另一项目、另一代码路径，不能推断为 MyTV 冻结根因。
- 本周没有 UTOS 修复、没有设备交叉测试、没有播放指标测量，也没有“修复成功”结论。

下一步必须记录：设备/固件/ABI、播放器与内核版本、脱敏源标识、协议/编码/封装、分辨率/帧率、网络/DNS/代理、缓冲、音频是否继续、PTS/DTS、解复用/解码/渲染状态、CPU/RAM/温控、Logcat 和复现频率。随后执行同源换播放器、同播放器换源、同源换设备、同设备换内核的控制变量测试。

## 9. 其他模块

- **Sports Live Center：** 本周没有足够证据确认新的授权体育直播 Provider；Bee-v1/v2、Smart IPTV Server、4GTV 系列继续分开记录，版权和来源授权未核实前不运行。
- **Retro Game Library：** 本周没有新增 Android TV/R700 可验证候选；ROM、BIOS 和游戏镜像继续坚持用户自有或明确授权来源。
- **Docker Operations：** 本周没有批准新的容器镜像或生产部署。继续要求固定版本/摘要、SBOM、最小权限、secrets、健康检查、资源限制、备份恢复和回滚。
- **Home Network：** OpenWrt 25.12.6 有安全修复；OpenClash/Mihomo 核心能力差异仍需按实际构建验证。
- **Cloud Drive：** OpenList 和 rclone 有源码变化，但未部署、未导入真实网盘凭据或测试大文件断点续传。

## 10. P0 / P1 / P2

### P0 — 阻塞真实验收
1. 采集 R700 精确 build fingerprint、SoC、ABI、MediaCodec、内存/存储、ADB 状态和网络路径。
2. 在隔离环境比较 Guovin 3.1.0 与 IPTV Checker；记录 Python 3.14、Docker 镜像摘要、依赖、配置迁移、M3U/EPG 输出和恢复。
3. 用合成/授权测试源打通 Cloud IPTV Hub 最小链路：源采集 → 标准化 → 去重 → 检测 → EPG → 版本化产物 → R2 发布 → D1 设备绑定 → UTV 客户端读取 → 回滚。每一步保存日志和哈希。
4. 完成 R700 播放基线与 MyTV 长时间冻结复现；优先 VLC/Media3 对照，不先改生产配置。
5. 在可丢弃 NAS/VM 验证 TrueNAS/OMV 安装恢复、文件系统、权限、备份和回滚；不得用生产磁盘试验。
6. 建立脱敏代理订阅 fixture，对 SubConverter-Extended 1.11.8 和候选 Rust 引擎做同条件基准与 Mihomo 配置加载验证。
7. Music Center WebDAV 同步必须先设计端到端加密和凭据排除策略。

### P1 — 架构与客户端验证
1. 设计 UTV 自有客户端二维码配对、设备撤销、订阅刷新和配置版本协议。
2. 单独验证 Kodi、VLC、TiviMate 的远程订阅能力；不支持者明确保留首次手动配置。
3. 对 Marco Movie 0.6.2、Jellyfin Android TV 0.20.0 做 ABI、签名、权限、遥控器、字幕、HDR、休眠恢复和长时间播放审查。
4. 对 OpenList/rclone 做只读挂载、HTTP Range、断点续传、缓存和恢复测试。

### P2 — 长期优化
1. 统一直播、影视、音乐的身份与来源溯源接口，但保持三类领域数据模型独立。
2. 完善 EPG、影视元数据、NFO、多语言、季/特别篇和用户修正的持久化策略。
3. 建立跨端播放指标基线后再制定冻结率、恢复率、切台耗时和复发率目标。

## 11. 本周执行边界

- 未安装、升级、侧载或部署任何 APK、Docker 镜像、NAS 服务、Cloudflare Worker/Pages/R2 或代理转换服务。
- 未操作 R700、Apple TV、NAS、磁盘、路由器、Windows 生产环境。
- 未导入真实订阅、Token、Cookie、网盘密码或私有播放地址。
- 未运行来源不明的插件、脚本或镜像。
- 未声称任何候选已部署、已实测或生产验收。
