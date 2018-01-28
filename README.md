- 安装elasticsearch（6.1.2）

- 安装id分词插件，到安装目录下执行：

`./bin/elasticsearch-plugin install https://github.com/medcl/elasticsearch-analysis-ik/releases/download/v6.1.2/elasticsearch-analysis-ik-6.1.2.zip`

- 重启elasticsearch

- 建立talent_index索引，类型talent_type，并初始化mappings，指定分词器：

        curl -XPUT 'localhost:9200/talent_index/?pretty' -H 'Content-Type: application/json' -d'
        {
          "mappings": {
            "talent_type" : {
              "properties" : {
                "doc" : {
                  "type" : "text",
                  "analyzer": "ik_max_word",
                  "search_analyzer": "ik_max_word"
                },
                "createTime" : {
                  "type" : "date"
                }
              }
            }
          }
        }
        '

- 修改application.properties中下列一些参数

        # elasticsearch client地址，只需其中一个可用节点即可
        spring.elasticsearch.host=127.0.0.1
        spring.elasticsearch.port=9300
        
        # 索引名与类型名
        spring.elasticsearch.talent.index=talent_index
        spring.elasticsearch.talent.type=talent_type
        
        # 扫描文件夹
        talent.scan.folder.path=/Users/le/temp/
        # 存放文件夹
        talent.repo.path=/Users/le/temp/repo/
        # 文件前缀（必须）
        talent.file.prestr=talent-
        
- 启动项目试试