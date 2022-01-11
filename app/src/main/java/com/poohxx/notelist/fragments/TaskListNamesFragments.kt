import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.poohxx.notelist.activities.MainApp
import com.poohxx.notelist.activities.TaskListActivity
import com.poohxx.notelist.databinding.FragmentTaskListNamesBinding

import com.poohxx.notelist.db.MainViewModel
import com.poohxx.notelist.db.TaskNameAdapter
import com.poohxx.notelist.dialogs.DeleteDialog
import com.poohxx.notelist.dialogs.NewListDialog
import com.poohxx.notelist.entities.NoteItem
import com.poohxx.notelist.entities.TaskListItem
import com.poohxx.notelist.entities.TaskListNames
import com.poohxx.notelist.fragments.BaseFragment
import com.poohxx.notelist.utils.TimeManager

class TaskListNamesFragment : BaseFragment(), TaskNameAdapter.Listener {
    private lateinit var binding: FragmentTaskListNamesBinding
    private lateinit var adapter: TaskNameAdapter


    private val mainViewModel: MainViewModel by activityViewModels {
        MainViewModel.MainViewModelFactory((context?.applicationContext as MainApp).dataBase)
    }

    override fun onClickNew() {
        NewListDialog.showDialog(activity as AppCompatActivity, object : NewListDialog.Listener {
            override fun onClick(name: String) {
                val taskListName = TaskListNames(
                    null,
                    name,
                    TimeManager.getCurrentTime(),
                    0,
                    0,
                    ""
                )
                mainViewModel.insertTaskListName(taskListName)
            }
        }, "")
    }




override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

}

override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
): View {
    binding = FragmentTaskListNamesBinding.inflate(inflater, container, false)
    return binding.root
}

override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initRcView()
    observer()
}

private fun initRcView() = with(binding) {
    rcView.layoutManager = LinearLayoutManager(activity)
    adapter = TaskNameAdapter(this@TaskListNamesFragment)
    rcView.adapter = adapter

}

private fun observer() {  mainViewModel.allTaskListNames.observe(viewLifecycleOwner, {
    adapter.submitList(it)
})
}


companion object {


    @JvmStatic
    fun newInstance() = TaskListNamesFragment()
}

    override fun deleteItem(id: Int) {
        DeleteDialog.showDialog(context as AppCompatActivity, object : DeleteDialog.Listener{
            override fun onClick() {
                mainViewModel.deleteTask(id)}
        })
    }
    override fun onEditItem(taskListName: TaskListNames) {
        NewListDialog.showDialog(activity as AppCompatActivity, object : NewListDialog.Listener {
            override fun onClick(name: String) {
                mainViewModel.updateTaskListName(taskListName.copy(name = name))
            }
        }, taskListName.name)
    }
    override fun onClickItem(taskListName: TaskListNames){
        val i = Intent(activity, TaskListActivity::class.java).apply{putExtra(TaskListActivity.TASK_LIST_NAME,taskListName)
        }
        startActivity(i)

    }

}


