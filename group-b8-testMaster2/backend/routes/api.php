<?php

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;
use App\Models\TaskList;
use App\Models\SprintList;
use App\Models\MemberList;
/*
|--------------------------------------------------------------------------
| API Routes
|--------------------------------------------------------------------------
|
| Here is where you can register API routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| is assigned the "api" middleware group. Enjoy building your API!
|
*/

// Route::middleware('auth:sanctum')->get('/user', function (Request $request) {
//     return $request->user();
// });

Route::post('/insert_task', function (Request $request) {
    if(!empty($request->all())){
        $model = new TaskList;
        $model->task_name = $request->task_name;
        $model->Description = $request->Description;
        $model->Assign_To = $request->Assign_To;
        $model->Story_Points = $request->Story_Points;
        $model->Status = $request->Status;
        $model->Deadline = $request->Deadline;
        $model->Tag = $request->Tag;
        $model->Priority = $request->Priority;
        $model->Task_Origin = $request->Task_Origin;
        $model->sprint_id = $request->sprint_id;
        $model->save();

        $models = TaskList::all();
        
        return response()->json($models);
    }
});

Route::post('/insert_sprint', function (Request $request) {
    if(!empty($request->all())){
        $model = new SprintList;
        $model->task_name = $request->task_name;
        $model->description = $request->description;
        $model->start_date = $request->start_date;
        $model->deadline = $request->deadline;
        $model->status = $request->status;

        $model->save();

        $models = SprintList::all();
        
        return response()->json($models);
    }
});

Route::post('/insert_member', function (Request $request) {
    if(!empty($request->all())){
        $model = new MemberList;
        $model->name = $request->name;
        $model->email = $request->email;

        $model->save();

        $models = MemberList::all();
        
        return response()->json($models);
    }
});


Route::post('/get_task', function (Request $request) {
   
    $models = TaskList::all();

    return response()->json(
        $models
    );
});

Route::post('/get_sprint', function (Request $request) {
   
    $models = SprintList::all();

    return response()->json(
        $models
    );
});

Route::post('/get_member', function (Request $request) {
   
    $models = MemberList::all();

    return response()->json(
        $models
    );
});


Route::post('/edit_task', function (Request $request) {
   
    $model = TaskList::find($request->id);
    $model->task_name = $request->task_name;
    $model->Description = $request->Description;
    $model->Assign_To = $request->Assign_To;
    $model->Story_Points = $request->Story_Points;
    $model->Status = $request->Status;
    $model->Deadline = $request->Deadline;
    $model->Tag = $request->Tag;
    $model->Priority = $request->Priority;
    $model->Task_Origin = $request->Task_Origin;
    if ($request->datetimelog != null and $model->datetimelog != NULL){
        $model->datetimelog = $model->datetimelog . ',' . $request->datetimelog;
    }
    if ($request->datetimelog != null and $model->datetimelog == NULL){
        $model->datetimelog = $request->datetimelog;
    }
    if ($request->hourstimelog != null and $model->hourstimelog != NULL){
        $model->hourstimelog = $model->hourstimelog . ',' . $request->hourstimelog;
    }
    if ($request->hourstimelog != null and $model->hourstimelog == NULL){
        $model->hourstimelog = $request->hourstimelog;
    }
    $model->sprint_id = $request->sprint_id;
    $model->save();

    $models = TaskList::all();
        
    return response()->json($models);
});

Route::post('/edit_sprint', function (Request $request) {
   
    $model = SprintList::find($request->id);
    $model->task_name = $request->task_name;
    $model->description = $request->description;
    $model->start_date = $request->start_date;
    $model->deadline = $request->deadline;
    $model->status = $request->status;
    $model->save();

    $models = SprintList::all();
        
    return response()->json($models);
});

Route::post('/edit_member', function (Request $request) {
   
    $model = MemberList::find($request->id);
    $model->name = $request->name;
    $model->email = $request->email;
    $model->save();

    $models = MemberList::all();
        
    return response()->json($models);
});


Route::post('/delete_task', function (Request $request) {
   
    $models = TaskList::find($request->id);
    $models->delete();

    return response()->json(
        $models
    );
});

Route::post('/delete_sprint', function (Request $request) {
   
    $models = SprintList::find($request->id);
    $models->delete();

    return response()->json(
        $models
    );
});

Route::post('/delete_member', function (Request $request) {
   
    $models = MemberList::find($request->id);
    $models->delete();

    return response()->json(
        $models
    );
});
