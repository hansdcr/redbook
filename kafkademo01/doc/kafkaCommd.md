### 查看topic

```
./kafka-topics.sh --list --zookeeper localhost:2181
```

### 创建topic

```
./kafka-topics.sh --zookeeper localhost:2181 --create --topic test --replication-factor 1 --partitions 1
```
