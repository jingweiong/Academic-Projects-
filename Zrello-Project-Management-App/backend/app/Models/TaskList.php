<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class TaskList extends Model
{
   protected $table = 'task_list';
   protected $fillable = ["*"];
   public $timestamps = true;
}