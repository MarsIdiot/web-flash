import cfgApi from '@/api/system/cfg'
import buyApi from '@/api/system/buy'
import { getApiUrl } from '@/utils/utils'
import permission from '@/directive/permission/index.js'

export default {
  name: 'buy',
  directives: { permission },
  data() {
    return {
      formVisible: false,
      formTitle: this.$t('buy.add'),
      isAdd: true,
      form: {
        buyId: '',
        buyName: '',
        buyData: ''
      },
      formEdit:{
        id:'',
        buyId: '',
        buyName: '',
        buyCode: '',
        buyCount: ''
      },
      formEditVisible: false,
      formEditTitle: this.$t('buy.edit'),
      isAddEdit: true,

      formCheckVisible: false,
      formCheckTitle: this.$t('buy.check'),
      isAddChek: true,
      formCheck:null,

      listQuery: {
        page: 1,
        limit: 20,
        buyName: null,
        buyCode: null
      },
      dialog:{
        show:false,
        title:'',
        content:'',
      },
      total: 0,
      list: null,
      listLoading: true,
      listCheckLoading: true,
      selRow: {}
    }
  },
  filters: {
    statusFilter(status) {
      const statusMap = {
        published: 'success',
        draft: 'gray',
        deleted: 'danger'
      }
      return statusMap[status]
    }
  },
  computed: {
    rules() {
      return {
        buyId: [
          { required: true, message: this.$t('buy.id') + this.$t('common.isRequired'), trigger: 'blur' },
          { min: 2, max: 20, message: this.$t('buy.id') + this.$t('buy.lengthValidation'), trigger: 'blur' }
        ],
        buyName: [
          { required: true, message: this.$t('buy.name') + this.$t('common.isRequired'), trigger: 'blur' },
          { min: 2, max: 200, message: this.$t('buy.Name') + this.$t('buy.lengthValidation'), trigger: 'blur' }
        ],
        buyData: [
          { required: true, message: this.$t('buy.data') + this.$t('common.isRequired'), trigger: 'blur' },
          { min: 2, max: 2000, message: this.$t('buy.data') + this.$t('buy.lengthValidationBuyData'), trigger: 'blur' }
        ],
        buyCode: [
          { required: true, message: this.$t('buy.code') + this.$t('common.isRequired'), trigger: 'blur' }
        ],
        buyCount: [
          { required: true, message: this.$t('buy.count') + this.$t('common.isRequired'), trigger: 'blur' }
        ],
      }
    }
  },
  created() {
    this.init()
  },
  methods: {
    init() {
      this.fetchData()
    },
    fetchData() {
      this.listLoading = true
      buyApi.getList(this.listQuery).then(response => {
        this.list = response.data.records
        this.listLoading = false
        this.total = response.data.total
      })
    },
    search() {
      this.listQuery.page = 1
      this.fetchData()
    },
    reset() {
      this.listQuery.buyName = ''
      this.listQuery.buyCode = ''
      this.listQuery.page = 1
      this.fetchData()
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    handleClose() {

    },
    fetchNext() {
      this.listQuery.page = this.listQuery.page + 1
      this.fetchData()
    },
    fetchPrev() {
      this.listQuery.page = this.listQuery.page - 1
      this.fetchData()
    },
    fetchPage(page) {
      this.listQuery.page = page
      this.fetchData()
    },
    changeSize(limit) {
      this.listQuery.limit = limit
      this.fetchData()
    },
    handleCurrentChange(currentRow, oldCurrentRow) {
      this.selRow = currentRow
    },
    resetForm() {
      this.form = {
        id: '',
        cfgName: '',
        cfgValue: '',
        cfgDesc: ''
      }
    },
    resetFormCheck() {
      this.formChek = {}
    },
    add() {
      this.resetForm()
      this.formTitle = this.$t('buy.add')
      this.formVisible = true
      this.isAdd = true
    },
    save() {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          const formData = {
            buyId: this.form.buyId,
            buyName: this.form.buyName,
            buyData: this.form.buyData
          }
          buyApi.transfer(formData).then(response => {
            this.$message({
              message: this.$t('common.optionSuccess'),
              type: 'success'
            })
            this.resetFormCheck()
            this.formCheck = response.data
            this.addCheck()
          })
        } else {
          return false
        }
      })
    },

    addCheck() {
      this.formTitle = this.$t('buy.check')
      this.formVisible = false
      this.isAdd = false
      this.formCheckVisible = true
      this.isAddChek = true
      this.listCheckLoading = false

    },
    saveCheck() {
      const formData = this.formCheck
      buyApi.add(formData).then(response => {
        this.$message({
          message: this.$t('common.optionSuccess'),
          type: 'success'
        })
        this.formCheckVisible = false
        this.fetchData()
        this.listLoading = false
      })
    },

    saveEdit() {
      const formEdit = this.formEdit
      buyApi.update(formEdit).then(response => {
        this.$message({
          message: this.$t('common.optionSuccess'),
          type: 'success'
        })
        this.formEditVisible = false
        this.fetchData()
        this.listLoading = false
      })
    },

    checkSel() {
      if (this.selRow && this.selRow.id) {
        return true
      }
      this.$message({
        message: this.$t('common.mustSelectOne'),
        type: 'warning'
      })
      return false
    },
    checkSelCheck() {
      if (this.selRow && this.selRow.id && !this.selRow.isCheck) {
        return true
      }
      this.$message({
        message: this.$t('common.checked'),
        type: 'warning'
      })
      return false
    },
    editItem(record){
      this.selRow= Object.assign({},record);
      this.edit()
    },
    edit() {
      if (this.checkSel()) {
        this.isAddEdit = false
        this.formEdit = this.selRow
        this.formEditTitle = this.$t('buy.edit')
        this.formEditVisible = true
      }
    },
    checkItem(record){
      this.selRow = record
      this.check()
    },
    check() {
      if (this.checkSelCheck()) {
        var id = this.selRow.id
        this.$confirm(this.$t('common.checkConfirm'), this.$t('common.tooltip'), {
          confirmButtonText: this.$t('button.submit'),
          cancelButtonText: this.$t('button.cancel'),
          type: 'warning'
        }).then(() => {
          const formData = {
            id: id,
            isCheck: 1,
            buyId: this.selRow.buyId,
            buyName: this.selRow.buyName,
            buyCode: this.selRow.buyCode,
            buyCount: this.selRow.buyCount
          }
          buyApi.update(formData).then(response => {
            this.$message({
              message: this.$t('common.optionSuccess'),
              type: 'success'
            })
            this.fetchData()
          })
        }).catch(() => {
        })
      }
    },
    removeItem(record){
      this.selRow = record
      this.remove()
    },
    remove() {
      if (this.checkSel()) {
        var id = this.selRow.id
        this.$confirm(this.$t('common.deleteConfirm'), this.$t('common.tooltip'), {
          confirmButtonText: this.$t('button.submit'),
          cancelButtonText: this.$t('button.cancel'),
          type: 'warning'
        }).then(() => {
          buyApi.remove(id).then(response => {
            this.$message({
              message: this.$t('common.optionSuccess'),
              type: 'success'
            })
            this.fetchData()
          })
        }).catch(() => {
        })
      }
    },
    exportXls() {
      cfgApi.exportXls(this.listQuery).then(response => {
        window.location.href= getApiUrl() + '/file/download?idFile='+response.data.id
      })

    },
    showCfgValDialog(data){
      this.dialog.content = data.cfgValue
      this.dialog.title= data.cfgName
      this.dialog.show=true
    }

  }
}
