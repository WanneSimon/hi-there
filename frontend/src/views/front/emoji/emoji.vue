<template>
  <div class="emoji-wrapper">
    <!-- <div style="position: sticky;margin-bottom: 0.3rem;" v-show="showConfig"> -->
    <div style="" v-if="!showConfig">
      <el-input v-model="name" class="search-input" 
          placeholder="文件名" @keydown.enter.native="searchClick" />
      <el-button @click="searchClick">搜索</el-button>
      <el-button @click="showConfig=true">设置</el-button>
      <!-- <el-button @click="loadClipboard">查看剪切板</el-button>
      <el-button @click="testCpClipboard">测试复制动图</el-button> -->
      
      <div class="content ">
        <!-- <div id="test-img-wrapper">
          <img id="test-img" tabindex="0" class=" nofocus" style="width: 200px;height:100px" aria-label="狐狸动图gif|UI|其他UI |siwuxie123_原创作品-站酷ZCOOL" alt="狐狸动图gif|UI|其他UI |siwuxie123_原创作品-站酷ZCOOL" src="https://img.zcool.cn/community/01d3f15adfe979a801214a617e684e.gif">
        </div> -->


        <el-scrollbar class="images " height=" calc( 100% - 6rem ) "  
            v-loading="loading" element-loading-background="rgba(0,0,0,0.2)">
          <div v-show="files && files.length>0" style="width:100%">
          <el-card v-for="item,index in files" :key="'file_'+index"
            :body-style="{ padding: '0px' }" class="img-item" v-loading="item._loading"> 
                <!-- <div > -->
                <!-- <el-image :src="'https://img.zcool.cn/community/01d3f15adfe979a801214a617e684e.gif'" :fit="'contain'" > -->
                <!-- <template v-if="item._url">
                  <img :src="item._url" :fit="'contain'"  />
                </template> -->
                <!--<el-image :src="item._url" :fit="'contain'" :preview-src-list="[item._url]" >
                    <template #error>{{ item._url }}</template>
                </el-image> -->
                <el-image :src="item._url" :fit="'contain'" :preview-src-list="[item._url]" >
                    <template #error>{{ item._url }}</template>
                </el-image>
                
                <p>{{item.name}}</p>
                <!-- <p>{{item._url}}</p> -->
                <div class="opt">
                  <!-- <el-button class="copy" type="primary" plain @click="copyToClipboard(item, 'image'+index)">复制</el-button> -->
                  <el-button class="copy" type="primary" plain @click="copyImgFile(item, 'image'+index)">复制</el-button>
                  <!-- <el-button class="copy" type="primary" plain @click="copyToClipboardNew(item, 'image'+index)">复制</el-button> -->
                </div>
          </el-card>
          </div>
        </el-scrollbar>

      </div>
    </div>

    <div v-if="showConfig">
      <!-- <el-button @click="showConfig=false">返回</el-button> -->
      <EmojiConfig @saved="refreshConfig" @close="showConfig=false"></EmojiConfig>
    </div>

  </div>
</template>

<!-- <script src="./emoji.ts" lang="ts"></script> -->
<script src="./emoji.js" ></script>

<style scoped lang="scss">
.emoji-wrapper{
  padding-top: 0.4rem;
}
.search-input{
  width: 20rem;
}

.content{
  min-height: 50vh;
  display: flex;
  justify-content: center;
  margin-top: 0.4rem;
}

.center-container{
  display: flex;
  // align-items: center;
  justify-content: center;
}

.images{
  position: fixed;
  width: 100%;

  :deep(.el-card__body) {
    height: 100%;
    width: 100%;
  }
  
  .img-item{
    width: 8rem;
    height: 8rem;
    justify-content: center;
    margin: 0rem 0.1rem;
    display: inline-block;
    position: relative;

    :deep(.el-image){
      height: calc( 100% - 3em );
      width: 100%;
    }

    &:hover{
      .opt{
        display: block;
      }
    }
  }
}

.opt{
  width: 100%;
  height: 2rem;
  position: absolute;
  display: flex;
  text-align: center;
  justify-content: center;
  // margin: 2px 0px;
  bottom: 0;
  display: none;

  .copy{
    width: 100%;
  }
}
</style>