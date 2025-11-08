#!/bin/bash

# ===============================
#  KAFKA KRaft Mode Setup Script
# ===============================

# 1️⃣ Generate a unique cluster ID
./bin/kafka-storage.sh random-uuid

# Example output:
#   4lvPpu_3SWyvu24gRaIT8g
# Copy the generated UUID into the next command below.

# 2️⃣ Format storage directory with the generated cluster ID
./bin/kafka-storage.sh format \
  -t 4lvPpu_3SWyvu24gRaIT8g \
  -c config/kraft/server.properties

# 3️⃣ Start Kafka server (in KRaft mode)
./bin/kafka-server-start.sh config/kraft/server.properties

# If needed to reformat / reset:
# rm -rf /tmp/kraft-combined-logs

# =======================================
#  TOPIC CREATION & PRODUCER / CONSUMER
# =======================================

# 4️⃣ Create topics
./bin/kafka-topics.sh --create \
  --topic topic1 \
  --partitions 3 \
  --replication-factor 1 \
  --bootstrap-server localhost:9092

./bin/kafka-topics.sh --create \
  --topic topic2 \
  --partitions 3 \
  --replication-factor 1 \
  --bootstrap-server localhost:9092

# 🆕 Added: topic3 creation
./bin/kafka-topics.sh --create \
  --topic topic3 \
  --partitions 3 \
  --replication-factor 1 \
  --bootstrap-server localhost:9092

# Verify topics
./bin/kafka-topics.sh --list --bootstrap-server localhost:9092

# ===========================================
#  PRODUCERS
# ===========================================

# 5️⃣ Simple producer (no keys)
./bin/kafka-console-producer.sh \
  --bootstrap-server localhost:9092 \
  --topic topic1

# 6️⃣ Producer with keys (key=value format)
./bin/kafka-console-producer.sh \
  --bootstrap-server localhost:9092 \
  --topic topic2 \
  --property "parse.key=true" \
  --property "key.separator=:"

# Example input:
# user1:Hello
# user2:Hi
# user1:How are you?

# ===========================================
#  CONSUMERS
# ===========================================

# 7️⃣ Consume values only
./bin/kafka-console-consumer.sh \
  --topic topic2 \
  --from-beginning \
  --bootstrap-server localhost:9092

# 8️⃣ Consume with keys and values
./bin/kafka-console-consumer.sh \
  --topic topic2 \
  --from-beginning \
  --bootstrap-server localhost:9092 \
  --property print.key=true \
  --property key.separator=" : "

# 9️⃣ (Optional) Print metadata
./bin/kafka-console-consumer.sh \
  --topic topic2 \
  --from-beginning \
  --bootstrap-server localhost:9092 \
  --property print.key=true \
  --property print.timestamp=true \
  --property print.partition=true \
  --property print.offset=true \
  --property key.separator=" : "

# ===========================================
#  TOPIC DETAILS
# ===========================================
./bin/kafka-topics.sh --describe --bootstrap-server localhost:9092