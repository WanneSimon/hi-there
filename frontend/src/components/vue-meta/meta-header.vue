<template>
  <metainfo>
    <template v-slot:title="{ content }">{{ (content ? content+' - ':'') }}</template>
    <template v-slot:keywords="{ keywords }">{{ (keywords ? keywords : '')  }}</template>
    <template v-slot:description="{ desc }">{{ (desc ? desc : '')  }}</template>
  </metainfo>
</template>

<script>
import { computed } from 'vue'
import { useMeta } from 'vue-meta'
export default {
  props: {
    // vue-meta 创建时用到的配置
    metaInfo: { 
      type: Object,
      default: () => {
        return {
          title: '', // 默认标题
          keywords: '',
          description: '',
        }
      }
    }
  },
  setup(props, context) {
    // console.log("props-metaInfo", props.metaInfo)
    // const metaInfo = props.metaInfo
    
    // const computedMeta = computed(() => metaInfo)
    // const { meta, onRemoved } = useMeta(computedMeta)

    // const { onRemoved } = useMeta(metaInfo)

    // onRemoved(() => {
      // Do something as soon as this metadata is removed,
      // eg because the component instance was destroyed
    // })
  },
  created () {
    const computedMeta = computed(() => this.metaInfo)
    useMeta(computedMeta)
  }

}
</script>
