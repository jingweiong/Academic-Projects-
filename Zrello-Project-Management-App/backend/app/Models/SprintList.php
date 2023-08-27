<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class SprintList extends Model
{
   protected $table = 'sprint_list';
   protected $fillable = ["*"];
   public $timestamps = true;
}