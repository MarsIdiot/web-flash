<template>
  <div class="app-container">
    <div class="block">
      <el-row :gutter="20">
        <el-col :span="4">
          <el-input v-model="listQuery.buyName" size="mini" :placeholder="$t('buy.name')"></el-input>
        </el-col>
        <el-col :span="4">
          <el-input v-model="listQuery.buyCode" size="mini" :placeholder="$t('buy.code')"></el-input>
        </el-col>
        <el-col :span="6">
          <el-button type="success" size="mini" icon="el-icon-search" @click.native="search">{{ $t('button.search') }}</el-button>
          <el-button type="primary" size="mini" icon="el-icon-refresh" @click.native="reset">{{ $t('button.reset') }}</el-button>
        </el-col>
      </el-row>
      <br>
      <el-row>
        <el-col :span="24">
          <el-button type="success" size="mini" icon="el-icon-plus" @click.native="add" v-permission="['/buy/add']">{{ $t('button.add') }}</el-button>
          <el-button type="primary" size="mini" icon="el-icon-edit" @click.native="edit" v-permission="['/buy/update']">{{ $t('button.edit') }}</el-button>
          <el-button type="danger" size="mini" icon="el-icon-edit" @click.native="check" v-permission="['/buy/check']">{{ $t('button.check') }}</el-button>
          <el-button type="danger" size="mini" icon="el-icon-delete" @click.native="remove" v-permission="['/buy/delete']">{{ $t('button.delete') }}</el-button>
          <el-button type="info" size="mini" icon="el-icon-document" @click.native="exportXls">{{ $t('button.export') }}</el-button>
        </el-col>
      </el-row>
    </div>

    <!--列表-->
    <el-table :data="list" v-loading="listLoading" element-loading-text="Loading" border fit highlight-current-row
              @current-change="handleCurrentChange">
      <el-table-column label="ID">
        <template slot-scope="scope">
          {{scope.row.id}}
        </template>
      </el-table-column>
      <el-table-column :label="$t('buy.code')">
        <template slot-scope="scope">
          {{scope.row.buyCode}}
        </template>
      </el-table-column>
      <el-table-column :label="$t('buy.count')">
        <template slot-scope="scope">
          {{scope.row.buyCount}}
        </template>
      </el-table-column>
      <el-table-column :label="$t('buy.id')">
        <template slot-scope="scope">
          {{scope.row.buyId}}
        </template>
      </el-table-column>
      <el-table-column :label="$t('buy.name')">
        <template slot-scope="scope">
          {{scope.row.buyName}}
        </template>
      </el-table-column>
      <el-table-column :label="$t('buy.createTime')">
        <template slot-scope="scope">
          {{scope.row.createTime}}
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button type="text" size="mini" icon="el-icon-edit" v-if="!scope.row.isCheck" @click.native="editItem(scope.row)" v-permission="['/buy/update']">{{ $t('button.edit') }}</el-button>
          <el-button type="text" size="mini" icon="el-icon-edit" v-if="!scope.row.isCheck" @click.native="checkItem(scope.row)" v-permission="['/buy/check']">{{ $t('button.check') }}</el-button>
          <el-button type="text" size="mini" icon="el-icon-delete" @click.native="removeItem(scope.row)" v-permission="['/buy/delete']">{{ $t('button.delete') }}</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      background
      layout="total, sizes, prev, pager, next, jumper"
      :page-sizes="[10, 20, 50, 100,500]"
      :page-size="listQuery.limit"
      :total="total"
      :current-page.sync="listQuery.page"
      @size-change="changeSize"
      @current-change="fetchPage"
      @prev-click="fetchPrev"
      @next-click="fetchNext">
    </el-pagination>
    <el-dialog
      :title="dialog.title"
      :visible.sync="dialog.show"
      width="50%" >
      <span>{{dialog.content}}</span>
      <span slot="footer" class="dialog-footer">
        <el-button @click.native="dialog.show = false">{{ $t('button.close') }}</el-button>
      </span>
    </el-dialog>

    <!--    添加页-->
    <el-dialog
      :title="formTitle"
      :visible.sync="formVisible"
      width="70%">
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-row>
          <el-col :span="12">
            <el-form-item :label="$t('buy.id')" prop="buyId">
              <el-input v-model="form.buyId" minlength=1></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('buy.name')" prop="buyName">
              <el-input v-model="form.buyName"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="$t('buy.data')" prop="buyData">
              <el-input v-model="form.buyData" minlength=1 type="textarea" :rows="5"></el-input>
            </el-form-item>
          </el-col>

        </el-row>
        <el-form-item>
          <el-button type="primary" @click="save">{{ $t('button.submit') }}</el-button>
          <el-button @click.native="formVisible = false">{{ $t('button.cancel') }}</el-button>
        </el-form-item>

      </el-form>
    </el-dialog>

    <!--购买信息确认页面    -->
    <el-dialog
      :title="formCheckTitle"
      :visible.sync="formCheckVisible"
      width="70%">
      <el-form  label-width="120px">
        <el-row>
          <el-table :data="formCheck" v-loading="listCheckLoading" element-loading-text="Loading" border fit highlight-current-row
                    @current-change="handleCurrentChange">
            <el-table-column :label="$t('buy.code')">
              <template slot-scope="scope">
                {{scope.row.buyCode}}
              </template>
            </el-table-column>
            <el-table-column :label="$t('buy.count')">
              <template slot-scope="scope">
                {{scope.row.buyCount}}
              </template>
            </el-table-column>
            <el-table-column :label="$t('buy.id')">
              <template slot-scope="scope">
                {{scope.row.buyId}}
              </template>
            </el-table-column>
            <el-table-column :label="$t('buy.name')">
              <template slot-scope="scope">
                {{scope.row.buyName}}
              </template>
            </el-table-column>
            <el-table-column label="操作">
              <template slot-scope="scope">
                <el-button type="text" size="mini" icon="el-icon-edit" @click.native="editItem(scope.row)" v-permission="['/buy/update']">{{ $t('button.edit') }}</el-button>
                <el-button type="text" size="mini" icon="el-icon-delete" @click.native="removeItem(scope.row)" v-permission="['/buy/delete']">{{ $t('button.delete') }}</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-row>

        <el-form-item>
          <el-button type="primary" @click="saveCheck">{{ $t('button.submit') }}</el-button>
          <el-button @click.native="formCheckVisible = false">{{ $t('button.cancel') }}</el-button>
        </el-form-item>

      </el-form>
    </el-dialog>

    <!--    修改页-->
    <el-dialog
      :title="formEditTitle"
      :visible.sync="formEditVisible"
      width="40%">
      <el-form ref="formEdit" :model="formEdit" :rules="rules" label-width="120px">
        <el-row>
          <el-col :span="24">
            <el-form-item :label="$t('buy.id')" prop="buyId">
              <el-input v-model="formEdit.buyId"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="$t('buy.name')" prop="buyName">
              <el-input v-model="formEdit.buyName"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="$t('buy.code')" prop="buyCode">
              <el-input v-model="formEdit.buyCode"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="$t('buy.count')" prop="buyCount">
              <el-input v-model="formEdit.buyCount"></el-input>
            </el-form-item>
          </el-col>

        </el-row>
        <el-form-item>
          <el-button type="primary" @click="saveEdit">{{ $t('button.submit') }}</el-button>
          <el-button @click.native="formEditVisible = false">{{ $t('button.cancel') }}</el-button>
        </el-form-item>

      </el-form>
    </el-dialog>


  </div>
</template>

<script src="./buy.js"></script>


<style rel="stylesheet/scss" lang="scss" scoped>
  @import "src/styles/common.scss";
</style>

