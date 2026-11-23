# Network Configuration Processing & Subscription Conversion

**归属：** UTV AI Technology Intelligence Center → Network Configuration Processing & Subscription Conversion  
**核查日期：** 2026-11-23  
**状态：** Study（研究模块）；未部署、未实测、未批准生产依赖。

## 1. 目标与边界

研究代理订阅解析、规范化、合并、模板注入、规则集处理、目标配置生成、版本管理和客户端校验能力，为 UTV 网络配置管理提供可审计、可回滚的处理链。

**严格边界：** 本模块处理 Clash/Mihomo 等代理客户端配置，不等同于 IPTV M3U/TXT/JSON/XMLTV/EPG 转换。两者可以复用通用解析、规范化、校验、模板、版本和分发基础设施，但必须保持独立数据模型和业务规则。

不绕过 DRM、账号鉴权、付费墙或地区授权；不向公共在线转换服务提交真实订阅。

## 2. 目标处理链

授权订阅输入
→ 凭据识别与脱敏
→ 协议解析
→ 节点规范化
→ 去重/重命名
→ 模板与规则集注入
→ 目标格式渲染
→ YAML/Schema/字段校验
→ 固定版本产物
→ Mihomo/Clash 客户端加载验证
→ 版本回滚与审计

节点连接质量、延迟、吞吐和可用性不由转换器性能替代；URL-Test、Fallback 和节点测速应由客户端或独立健康检查模块承担。

## 3. 上游候选

| 项目 | 本周核查 | 许可证 / 状态 | 结论 |
|---|---|---|---|
| [Aethersailor/SubConverter-Extended](https://github.com/Aethersailor/SubConverter-Extended) | v1.11.8，2026-11-21 | GPL-3.0 / Study | 当前最活跃的重点候选；发布记录包含 VLESS Reality 隐式 SNI 等解析修正。需按发布版本核实协议字段，不推断所有客户端都兼容。 |
| [tindy2013/subconverter](https://github.com/tindy2013/subconverter) | 本周无新正式版本；最近可见提交早于本周 | 上游许可与依赖需逐项复核 / Study | 作为原始 C++ 上游参考，重点看解析边界和模板兼容，不直接假设其支持新协议字段。 |
| [lonelam/subconverter-rs](https://github.com/lonelam/subconverter-rs) | 最近可见提交 2026-08-29 | 许可证、依赖和发布方式需逐项复核 / Study | Rust 实现候选；没有同条件性能数据，不宣称更快或更省资源。 |

## 4. 协议与输出兼容矩阵

重点检查：
- VLESS Reality，包括 xtls-rprx-vision 和隐式 SNI 解析。
- VLESS XHTTP。
- Hysteria2。
- Shadowsocks。
- 多订阅合并、节点去重/重命名、策略组、Rule Provider、规则集展开、UDP/XUDP/TFO、远程订阅刷新和错误处理。

每项支持必须分别记录：
1. 官方文档声明；
2. 当前源码解析/输出实现；
3. 生成配置是否通过目标客户端 YAML/Schema 校验；
4. 实际客户端是否成功加载；
5. 节点连接和传输表现是否单独验证。

仅能输出字段不等于客户端可用；配置可加载不等于节点可连接。

## 5. 参数和模板验证

expand、emoji、list、insert、new_name、udp、tfo 以及 ACL4SSR Full/Mini 等模板，必须按单变量对照测试。

特别注意：
- expand=false 可能减少主配置中的规则展开内容，但依赖客户端对远程 Rule Provider 的支持。
- 不能据此推断总下载量、运行内存、CPU 或连接稳定性必然下降。
- UDP/TFO/XUDP 等字段必须与目标 Mihomo/Clash 核心版本和构建选项对应。
- 不把节点测速结果混入转换耗时。

## 6. 可复现性能基准

统一使用同一份脱敏测试订阅、相同节点数量、相同模板/规则、相同输出目标、相同 CPU/RAM/操作系统和相同客户端版本。

至少记录：
- 冷启动与热启动转换耗时；
- 吞吐量、CPU、峰值内存；
- 输出配置字节数；
- 输入节点数、保留节点数和节点保留率；
- 解析错误、字段丢失和 YAML 校验失败率；
- 客户端加载结果；
- 重复次数、样本数和环境指纹。

本周没有执行任何基准测试。上游提交中的基准数据只能标记为“上游自测”，不能当作 UTV 环境数据。

## 7. 安全与隐私

订阅 URL、UUID、密码、Token、Reality 参数和用户标签均可能是敏感信息。

强制要求：
- 仅使用脱敏 fixture 做研究和基准。
- 优先本地或自托管转换；公共服务不得接收真实订阅。
- 日志只记录脱敏后的协议类型、字段缺失、错误类别和版本，不记录完整凭据。
- 固定容器版本与镜像摘要，审查 SBOM/CVE、签名、依赖和构建来源。
- 最小权限、密钥分离、访问控制、请求限流、审计和可回滚配置。
- 未经明确批准，不部署服务、不导入真实订阅、不运行未知代码、不修改客户端配置。

## 8. 与 UTV IPTV Source Engine 的复用边界

可复用：
- 通用解析器框架、输入校验、规范化流水线、模板系统、版本化产物、审计日志和分发接口。

不可混用：
- 代理节点/协议模型；
- IPTV 频道/线路模型；
- EPG 节目表与 Rule Provider；
- 代理 URL-Test 与直播流健康检测；
- 代理配置凭据与 IPTV 订阅授权。

UTV IPTV Source Engine 仍独立负责直播源采集、频道标准化、去重、可用性检测、EPG 匹配、M3U/TXT/JSON/XMLTV 生成和播放器适配。

## 9. 本周版本快照

- SubConverter-Extended v1.11.8：2026-11-21。
- Mihomo v1.19.34：2026-11-11；上游支持 Snell v6 等能力，但需确认 OpenClash 实际使用的核心。
- OpenClash 0.48.002：本周未发现更新的正式版本证据。
- OpenWrt 25.12.6：2026-10-18；包含多项安全修复，升级前必须核对设备 target 和恢复方案。

官方来源：
- [SubConverter-Extended Releases](https://github.com/Aethersailor/SubConverter-Extended/releases)
- [Mihomo Releases](https://github.com/MetaCubeX/mihomo/releases)
- [OpenClash Releases](https://github.com/vernesong/OpenClash/releases)
- [OpenWrt 25.12.6](https://github.com/openwrt/openwrt/releases/tag/v25.12.6)

## 10. P0 验收

1. 固定一份不含真实凭据的协议覆盖 fixture。
2. 逐项核验 Reality、XHTTP、Hysteria2、Shadowsocks 的解析和输出字段。
3. 对 SubConverter-Extended 与 Rust 候选执行同环境重复基准。
4. 在隔离环境验证 Mihomo/Clash 配置加载；区分核心版本、OpenClash 插件版本和 Smart/Meta 核心构建。
5. 验证 expand、UDP、TFO、Rule Provider 的单变量差异。
6. 验证凭据脱敏、日志清理、配置版本、备份和回滚。

未完成以上验收前，保持 Study，不作为 UTV 生产依赖。
