# Change control

1. Establish baseline: branch, working tree, behavior and relevant tests.
2. Define scope and non-goals.
3. Assess compatibility, data, security and rollback impact.
4. Make the smallest coherent change.
5. Run focused checks, then regression proportional to risk.
6. Review diff for secrets, unrelated edits, dependency drift and scope expansion.
7. Record outcome and known gaps.

Freeze only after evidence and acceptance pass. Reopen for factual error, reproducible test conflict, security issue or acceptance blocker; otherwise queue improvement for a later version.
