<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class MemberList extends Model
{
   protected $table = 'member_list';
   protected $fillable = ["*"];
   public $timestamps = true;
}