// const emojiOp = window["JPP"]["emojiOp"]
const emojiOp = window["jpp"]

import { ElNotification } from 'element-plus'
const Noti = ElNotification

export default {
  name: 'Emoji',
  components: { },
  emit: [ "saved", "close" ],
  data () {
    return {
      config: null, // 从

      emojis: [], // 
      loading: false,
    }
  },
  async mounted() { 
    await this.getConfig()
  },
  methods: {
    // 获取配置
    async getConfig() {
      this.loading = true
      // let conf =  await Config().then(res => res)
      let conf = await emojiOp.get()

      this.config = conf
      this.emojis = conf || []
      

      for(let i=this.emojis.length; i<3; i++) {
        this.pushNewConfig()
      }

      this.loading = false
    },

    // 
    pushNewConfig() {
      if(!this.emojis) {
        this.emojis = []
      }

      this.emojis.push("")
    },

    async selectFolder(index) {
      let folder = await emojiOp.selectFolder().then(res => res)
      if(folder) {
        this.emojis[index] = folder
      }
    },
    // async selectFolder() {
    //   console.log(arguments)
    // },

    deleteConfig(index) {
      this.emojis.splice(index, 1)
    },
    resetConfig() {
      // console.log("emojis", this.emojis)
      // this.emojis = this.config.Emojis
      // console.log("emojis", this.emojis, this.config.Emojis)
    },

    async saveConfig() {
      console.log("saveConfig", this.emojis)
      this.emojis = this.emojis.filter(e => e)
      let re = await emojiOp.savePaths(this.emojis).then(res => res)
      // console.log("Save", re)
      if(re) {
        Noti.success({ message: "保存成功", position: 'bottom-right',})
        this.$emit("saved") // 不刷新自己组件的信息
      } else {
        Noti.error({ message: "保存失败", position: 'bottom-right',})
      }
    },

    back() {
      this.$emit("close") // 关闭当前组件
    }
  }

}