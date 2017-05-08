###Linux
----
net.ipv4.tcp_tw_reuse = 1

net.ipv4.tcp_fin_timeout = 15

net.core.rmem_max = 16777216

net.core.wmem_max = 16777216

net.ipv4.tcp_rmem = 4096 87380 16777216

net.ipv4.tcp_wmem = 4096 65536 16777216

net.core.netdev_max_backlog = 4096

net.core.somaxconn = 4096

net.ipv4.tcp_max_syn_backlog = 20480

net.ipv4.tcp_syncookies = 1

net.ipv4.tcp_max_tw_buckets = 360000

net.ipv4.tcp_no_metrics_save = 1

net.ipv4.tcp_syn_retries = 2

net.ipv4.tcp_synack_retries = 2
